package com.musicstream.library.domain.model;

import java.util.Objects;
import java.util.UUID;

/** Identity of a playlist (a local entity inside the MusicLibrary aggregate). */
public final class PlaylistId {

    private final String value;

    private PlaylistId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("playlist id must not be blank");
        }
        this.value = value;
    }

    public static PlaylistId of(String value) {
        return new PlaylistId(value);
    }

    public static PlaylistId newId() {
        return new PlaylistId(UUID.randomUUID().toString());
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj instanceof PlaylistId other && value.equals(other.value);
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
