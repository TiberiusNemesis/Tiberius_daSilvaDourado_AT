package com.musicstream.billing.domain.model;

import java.util.Objects;
import java.util.UUID;

/** Identity of an authorized transaction. */
public final class TransactionId {

    private final String value;

    private TransactionId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("transaction id must not be blank");
        }
        this.value = value;
    }

    public static TransactionId of(String value) {
        return new TransactionId(value);
    }

    public static TransactionId newId() {
        return new TransactionId(UUID.randomUUID().toString());
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj instanceof TransactionId other && value.equals(other.value);
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
