package com.musicstream.library.domain.model;

import java.util.Objects;

/**
 * Identity of a library owner, expressed in this context's own terms.
 *
 * It happens to carry the same string a listener is known by elsewhere, but the
 * Anticorruption Layer is what maps between the two worlds. The Music Library
 * never imports the Listener Account model.
 */
public final class OwnerId {

    private final String value;

    private OwnerId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("owner id must not be blank");
        }
        this.value = value;
    }

    public static OwnerId of(String value) {
        return new OwnerId(value);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj instanceof OwnerId other && value.equals(other.value);
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
