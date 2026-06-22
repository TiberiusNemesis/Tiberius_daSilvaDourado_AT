package com.musicstream.billing.domain.model;

import com.musicstream.shared.Money;

import java.time.Duration;
import java.time.Instant;

/**
 * An authorized payment, recorded in the {@link Subscriber}'s history. Entity
 * with identity. It carries the few facts the anti-fraud rules reason about:
 * how much, to whom, and when.
 */
public class Transaction {

    private final TransactionId id;
    private final Money amount;
    private final Merchant merchant;
    private final Instant occurredAt;

    private Transaction(TransactionId id, Money amount, Merchant merchant, Instant occurredAt) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.occurredAt = occurredAt;
    }

    static Transaction record(Money amount, Merchant merchant, Instant occurredAt) {
        return new Transaction(TransactionId.newId(), amount, merchant, occurredAt);
    }

    public static Transaction rehydrate(TransactionId id, Money amount, Merchant merchant, Instant occurredAt) {
        return new Transaction(id, amount, merchant, occurredAt);
    }

    /** True when this transaction happened within {@code window} before {@code reference}. */
    public boolean happenedWithin(Duration window, Instant reference) {
        Instant from = reference.minus(window);
        return !occurredAt.isBefore(from) && !occurredAt.isAfter(reference);
    }

    /** Two transactions are "similar" when they share the same amount and merchant. */
    public boolean isSimilarTo(Money otherAmount, Merchant otherMerchant) {
        return amount.equals(otherAmount) && merchant.equals(otherMerchant);
    }

    public TransactionId id() {
        return id;
    }

    public Money amount() {
        return amount;
    }

    public Merchant merchant() {
        return merchant;
    }

    public Instant occurredAt() {
        return occurredAt;
    }
}
