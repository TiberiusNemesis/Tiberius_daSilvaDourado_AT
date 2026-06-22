package com.musicstream.billing.application;

import com.musicstream.billing.acl.AccountGateway;
import com.musicstream.billing.acl.BillingCustomer;
import com.musicstream.billing.domain.model.Merchant;
import com.musicstream.billing.domain.model.Subscriber;
import com.musicstream.billing.domain.model.SubscriberId;
import com.musicstream.billing.domain.repository.SubscriberRepository;
import com.musicstream.billing.domain.service.AuthorizationResult;
import com.musicstream.billing.domain.service.TransactionAuthorizationService;
import com.musicstream.billing.domain.service.TransactionRequest;
import com.musicstream.shared.Money;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;

/**
 * Application Service that drives a single authorization: resolve the customer
 * through the ACL, hand the aggregate and request to the {@link
 * TransactionAuthorizationService} (Domain Service), and persist the result.
 */
public class TransactionAuthorizationAppService implements TransactionAuthorization {

    private final SubscriberRepository subscribers;
    private final AccountGateway accountGateway;
    private final TransactionAuthorizationService authorizationService;
    private final Clock clock;

    public TransactionAuthorizationAppService(SubscriberRepository subscribers,
                                              AccountGateway accountGateway,
                                              TransactionAuthorizationService authorizationService,
                                              Clock clock) {
        this.subscribers = subscribers;
        this.accountGateway = accountGateway;
        this.authorizationService = authorizationService;
        this.clock = clock;
    }

    @Override
    public AuthorizationResult authorize(String listenerId, BigDecimal amount, String merchant, Instant occurredAt) {
        BillingCustomer customer = accountGateway.findCustomer(listenerId)
                .filter(BillingCustomer::active)
                .orElseThrow(() -> new UnknownOrInactiveCustomerException(listenerId));
        SubscriberId subscriberId = customer.id();
        Subscriber subscriber = subscribers.findById(subscriberId)
                .orElseGet(() -> Subscriber.register(subscriberId));

        Instant when = occurredAt != null ? occurredAt : clock.instant();
        TransactionRequest request = new TransactionRequest(Money.of(amount), Merchant.of(merchant), when);

        AuthorizationResult result = authorizationService.authorize(subscriber, request);
        if (result.isApproved()) {
            subscribers.save(subscriber);
        }
        return result;
    }
}
