package com.musicstream.app.infrastructure.billing;

import com.musicstream.billing.domain.model.CardStatus;
import com.musicstream.billing.domain.model.CreditCard;
import com.musicstream.billing.domain.model.Plan;
import com.musicstream.billing.domain.model.Subscriber;
import com.musicstream.billing.domain.model.SubscriberId;
import com.musicstream.billing.domain.model.Subscription;
import com.musicstream.billing.domain.model.SubscriptionStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.Instant;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA persistence model for the {@link Subscriber} aggregate. Holds the
 * (optional) credit card and subscription as flat columns and the transaction
 * history as a child collection, translating to and from the rich aggregate.
 */
@Entity
@Table(name = "subscriber")
class SubscriberEntity {

    @Id
    private String id;

    private String cardNumber;
    private String cardHolder;
    private String cardExpiry;
    @Enumerated(EnumType.STRING)
    private CardStatus cardStatus;

    @Enumerated(EnumType.STRING)
    private Plan plan;
    private Instant subscriptionStartedAt;
    @Enumerated(EnumType.STRING)
    private SubscriptionStatus subscriptionStatus;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "subscriber_id")
    private List<TransactionEntity> history = new ArrayList<>();

    protected SubscriberEntity() {
        // required by JPA
    }

    static SubscriberEntity fromDomain(Subscriber subscriber) {
        SubscriberEntity entity = new SubscriberEntity();
        entity.id = subscriber.id().value();
        subscriber.creditCard().ifPresent(card -> {
            entity.cardNumber = card.number();
            entity.cardHolder = card.holderName();
            entity.cardExpiry = card.expiry().toString();
            entity.cardStatus = card.status();
        });
        subscriber.activeSubscription().ifPresent(subscription -> {
            entity.plan = subscription.plan();
            entity.subscriptionStartedAt = subscription.startedAt();
            entity.subscriptionStatus = subscription.status();
        });
        entity.history = subscriber.history().stream()
                .map(TransactionEntity::fromDomain)
                .collect(java.util.stream.Collectors.toCollection(ArrayList::new));
        return entity;
    }

    Subscriber toDomain() {
        CreditCard card = null;
        if (cardNumber != null) {
            card = CreditCard.of(cardNumber, cardHolder, YearMonth.parse(cardExpiry), cardStatus);
        }
        Subscription subscription = null;
        if (plan != null && subscriptionStatus != null) {
            subscription = Subscription.rehydrate(plan, subscriptionStartedAt, subscriptionStatus);
        }
        return Subscriber.rehydrate(
                SubscriberId.of(id),
                card,
                subscription,
                history.stream().map(TransactionEntity::toDomain).toList());
    }
}
