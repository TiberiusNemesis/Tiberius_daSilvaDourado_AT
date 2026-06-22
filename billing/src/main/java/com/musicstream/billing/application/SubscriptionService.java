package com.musicstream.billing.application;

import com.musicstream.billing.acl.AccountGateway;
import com.musicstream.billing.acl.BillingCustomer;
import com.musicstream.billing.domain.model.CardStatus;
import com.musicstream.billing.domain.model.CreditCard;
import com.musicstream.billing.domain.model.Plan;
import com.musicstream.billing.domain.model.Subscriber;
import com.musicstream.billing.domain.model.SubscriberId;
import com.musicstream.billing.domain.model.Subscription;
import com.musicstream.billing.domain.repository.SubscriberRepository;

import java.time.Clock;
import java.time.YearMonth;

/**
 * Application Service for subscriptions. It validates the listener through the
 * Anticorruption Layer, loads (or opens) their {@link Subscriber} aggregate, and
 * lets the aggregate enforce the one-active-plan and valid-card invariants.
 */
public class SubscriptionService implements SubscriptionManagement {

    private final SubscriberRepository subscribers;
    private final AccountGateway accountGateway;
    private final Clock clock;

    public SubscriptionService(SubscriberRepository subscribers, AccountGateway accountGateway, Clock clock) {
        this.subscribers = subscribers;
        this.accountGateway = accountGateway;
        this.clock = clock;
    }

    @Override
    public void registerCreditCard(String listenerId, String cardNumber, String holderName,
                                   YearMonth expiry, boolean active) {
        Subscriber subscriber = loadOrOpen(listenerId);
        CardStatus status = active ? CardStatus.ACTIVE : CardStatus.INACTIVE;
        subscriber.attachCreditCard(CreditCard.of(cardNumber, holderName, expiry, status));
        subscribers.save(subscriber);
    }

    @Override
    public Subscription subscribe(String listenerId, Plan plan) {
        Subscriber subscriber = loadOrOpen(listenerId);
        Subscription subscription = subscriber.subscribeTo(plan, clock.instant());
        subscribers.save(subscriber);
        return subscription;
    }

    private Subscriber loadOrOpen(String listenerId) {
        BillingCustomer customer = accountGateway.findCustomer(listenerId)
                .filter(BillingCustomer::active)
                .orElseThrow(() -> new UnknownOrInactiveCustomerException(listenerId));
        SubscriberId subscriberId = customer.id();
        return subscribers.findById(subscriberId).orElseGet(() -> Subscriber.register(subscriberId));
    }
}
