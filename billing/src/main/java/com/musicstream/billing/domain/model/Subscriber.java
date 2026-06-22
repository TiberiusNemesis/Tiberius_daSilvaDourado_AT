package com.musicstream.billing.domain.model;

import com.musicstream.shared.Money;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Aggregate Root of the Core billing context. It owns the subscriber's credit
 * card, their single active subscription and the history of authorized
 * transactions, and it protects the business invariants:
 *
 * <ul>
 *   <li>only one active plan at a time;</li>
 *   <li>a paid plan requires a valid credit card.</li>
 * </ul>
 *
 * The anti-fraud rules that gate <em>new</em> transactions are evaluated by the
 * {@code TransactionAuthorizationService} (a Domain Service), which reads this
 * aggregate's history through the query methods below and, on approval, asks it
 * to {@link #recordAuthorized}.
 */
public class Subscriber {

    private final SubscriberId id;
    private CreditCard creditCard;
    private Subscription activeSubscription;
    private final List<Transaction> history;

    private Subscriber(SubscriberId id, CreditCard creditCard, Subscription activeSubscription,
                       List<Transaction> history) {
        this.id = id;
        this.creditCard = creditCard;
        this.activeSubscription = activeSubscription;
        this.history = history;
    }

    public static Subscriber register(SubscriberId id) {
        return new Subscriber(id, null, null, new ArrayList<>());
    }

    public static Subscriber rehydrate(SubscriberId id, CreditCard creditCard, Subscription activeSubscription,
                                       List<Transaction> history) {
        return new Subscriber(id, creditCard, activeSubscription, new ArrayList<>(history));
    }

    public void attachCreditCard(CreditCard card) {
        this.creditCard = card;
    }

    /** Subscribe to a plan, enforcing the single-active-plan and valid-card rules. */
    public Subscription subscribeTo(Plan plan, Instant when) {
        if (hasActivePlan()) {
            throw new AlreadyHasActivePlanException(id);
        }
        if (plan.isPaid() && (creditCard == null || !creditCard.isValid())) {
            throw new MissingValidCreditCardException(id);
        }
        this.activeSubscription = Subscription.start(plan, when);
        return activeSubscription;
    }

    public void cancelSubscription() {
        if (activeSubscription != null) {
            activeSubscription.cancel();
            activeSubscription = null;
        }
    }

    /** Append an approved transaction to the history. Called only after the rules pass. */
    public Transaction recordAuthorized(Money amount, Merchant merchant, Instant occurredAt) {
        Transaction transaction = Transaction.record(amount, merchant, occurredAt);
        history.add(transaction);
        return transaction;
    }

    // ---- Queries used by the anti-fraud rules -------------------------------

    public boolean hasActivePlan() {
        return activeSubscription != null && activeSubscription.isActive();
    }

    public boolean hasActiveCard() {
        return creditCard != null && creditCard.isActive();
    }

    public List<Transaction> transactionsWithin(Duration window, Instant reference) {
        return history.stream()
                .filter(t -> t.happenedWithin(window, reference))
                .toList();
    }

    public long countSimilarWithin(Money amount, Merchant merchant, Duration window, Instant reference) {
        return history.stream()
                .filter(t -> t.happenedWithin(window, reference))
                .filter(t -> t.isSimilarTo(amount, merchant))
                .count();
    }

    public SubscriberId id() {
        return id;
    }

    public Optional<CreditCard> creditCard() {
        return Optional.ofNullable(creditCard);
    }

    public Optional<Subscription> activeSubscription() {
        return Optional.ofNullable(activeSubscription);
    }

    public List<Transaction> history() {
        return Collections.unmodifiableList(history);
    }
}
