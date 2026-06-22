package com.musicstream.app.infrastructure.library;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

/** JPA persistence model for a playlist (child of the music library). */
@Entity
@Table(name = "playlist")
class PlaylistEntity {

    @Id
    private String id;
    private String name;

    @ElementCollection
    @CollectionTable(name = "playlist_track", joinColumns = @JoinColumn(name = "playlist_id"))
    @OrderColumn(name = "position")
    @Column(name = "track_id")
    private List<String> trackIds = new ArrayList<>();

    protected PlaylistEntity() {
        // required by JPA
    }

    PlaylistEntity(String id, String name, List<String> trackIds) {
        this.id = id;
        this.name = name;
        this.trackIds = new ArrayList<>(trackIds);
    }

    String id() {
        return id;
    }

    String name() {
        return name;
    }

    List<String> trackIds() {
        return trackIds;
    }
}
