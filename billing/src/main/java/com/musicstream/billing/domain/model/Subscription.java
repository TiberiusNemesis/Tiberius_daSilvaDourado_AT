package com.musicstream.billing.domain.model;

import java.time.Instant;

/**
 * A subscription to a {@link Plan}. Entity inside the {@link Subscriber}
 * aggregate; it is created active and can only be cancelled, never mutated into
 * another plan (a plan change is "cancel + subscribe").
 */
public class Subscription {

    private final Plan plan;
    private final Instant startedAt;
    private SubscriptionStatus status;

    private Subscription(Plan plan, Instant startedAt, SubscriptionStatus status) {
        this.plan = plan;
        this.startedAt = startedAt;
        this.status = status;
    }

    static Subscription start(Plan plan, Instant when) {
        return new Subscription(plan, when, SubscriptionStatus.ACTIVE);
    }

    public static Subscription rehydrate(Plan plan, Instant startedAt, SubscriptionStatus status) {
        return new Subscription(plan, startedAt, status);
    }

    void cancel() {
        this.status = SubscriptionStatus.CANCELLED;
    }

    public boolean isActive() {
        return status == SubscriptionStatus.ACTIVE;
    }

    public Plan plan() {
        return plan;
    }

    public Instant startedAt() {
        return startedAt;
    }

    public SubscriptionStatus status() {
        return status;
    }
}
