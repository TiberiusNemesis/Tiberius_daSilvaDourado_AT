package com.musicstream.billing.domain.model;

import java.util.Objects;

/** The counterparty of a transaction (e.g. an in-app store or label). Value Object. */
public final class Merchant {

    private final String name;

    private Merchant(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("merchant must not be blank");
        }
        this.name = name.trim();
    }

    public static Merchant of(String name) {
        return new Merchant(name);
    }

    public String name() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj instanceof Merchant other && name.equalsIgnoreCase(other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase());
    }

    @Override
    public String toString() {
        return name;
    }
}
