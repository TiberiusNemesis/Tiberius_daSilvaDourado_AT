package com.musicstream.billing.domain.model;

import java.util.Objects;

/**
 * Identity of a subscriber, in the billing context's own language. It carries
 * the same value the listener is known by elsewhere, but the mapping is the
 * Anticorruption Layer's job — billing never imports the account model.
 */
public final class SubscriberId {

    private final String value;

    private SubscriberId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("subscriber id must not be blank");
        }
        this.value = value;
    }

    public static SubscriberId of(String value) {
        return new SubscriberId(value);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj instanceof SubscriberId other && value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
