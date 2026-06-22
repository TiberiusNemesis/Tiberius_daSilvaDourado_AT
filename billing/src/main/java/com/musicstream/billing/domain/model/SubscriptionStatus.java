package com.musicstream.billing.domain.model;

/** Lifecycle of a subscription. Only one ACTIVE subscription may exist per subscriber. */
public enum SubscriptionStatus {
    ACTIVE,
    CANCELLED
}
