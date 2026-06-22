package com.musicstream.library.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Entity local to the {@link MusicLibrary} aggregate. It has its own identity
 * ({@link PlaylistId}) but is only ever reached through the aggregate root,
 * which guards the invariant "no duplicate track within a playlist".
 */
public class Playlist {

    private final PlaylistId id;
    private final String name;
    private final List<TrackId> tracks;

    private Playlist(PlaylistId id, String name, List<TrackId> tracks) {
        this.id = id;
        this.name = name;
        this.tracks = tracks;
    }

    static Playlist create(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("playlist name must not be blank");
        }
        return new Playlist(PlaylistId.newId(), name.trim(), new ArrayList<>());
    }

    public static Playlist rehydrate(PlaylistId id, String name, List<TrackId> tracks) {
        return new Playlist(id, name, new ArrayList<>(tracks));
    }

    void add(TrackId track) {
        if (!tracks.contains(track)) {
            tracks.add(track);
        }
    }

    public PlaylistId id() {
        return id;
    }

    public String name() {
        return name;
    }

    public List<TrackId> tracks() {
        return Collections.unmodifiableList(tracks);
    }
}
