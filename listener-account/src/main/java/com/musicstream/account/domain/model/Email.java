package com.musicstream.account.domain.model;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Email address Value Object. Validates its own format on construction, so an
 * {@link Email} instance is, by definition, always well-formed.
 */
public final class Email {

    private static final Pattern FORMAT = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private final String value;

    private Email(String value) {
        this.value = value;
    }

    public static Email of(String raw) {
        if (raw == null || !FORMAT.matcher(raw).matches()) {
            throw new IllegalArgumentException("invalid email: " + raw);
        }
        return new Email(raw.toLowerCase());
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj instanceof Email other && value.equals(other.value);
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
