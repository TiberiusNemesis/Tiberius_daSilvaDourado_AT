package com.musicstream.account.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Stable identity of a listener. Value Object: two ids are equal iff their
 * underlying value is equal. This is the identity that other contexts reference
 * (through their own Anticorruption Layers) to talk about "the same listener".
 */
public final class ListenerId {

    private final String value;

    private ListenerId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("listener id must not be blank");
        }
        this.value = value;
    }

    public static ListenerId of(String value) {
        return new ListenerId(value);
    }

    public static ListenerId newId() {
        return new ListenerId(UUID.randomUUID().toString());
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj instanceof ListenerId other && value.equals(other.value);
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
