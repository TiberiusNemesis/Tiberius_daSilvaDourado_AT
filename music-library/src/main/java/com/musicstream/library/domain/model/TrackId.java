package com.musicstream.library.domain.model;

import java.util.Objects;

/** Identity of a track in the catalog, as referenced by the library. */
public final class TrackId {

    private final String value;

    private TrackId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("track id must not be blank");
        }
        this.value = value;
    }

    public static TrackId of(String value) {
        return new TrackId(value);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj instanceof TrackId other && value.equals(other.value);
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
