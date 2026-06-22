package com.musicstream.app.infrastructure.library;

import com.musicstream.library.domain.model.MusicLibrary;
import com.musicstream.library.domain.model.OwnerId;
import com.musicstream.library.domain.model.Playlist;
import com.musicstream.library.domain.model.PlaylistId;
import com.musicstream.library.domain.model.TrackId;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * JPA persistence model for the music library aggregate. Maps the favorites and
 * playlists, translating to and from the rich {@link MusicLibrary} aggregate.
 */
@Entity
@Table(name = "music_library")
class MusicLibraryEntity {

    @Id
    private String ownerId;

    @ElementCollection
    @CollectionTable(name = "favorite_track", joinColumns = @JoinColumn(name = "owner_id"))
    @Column(name = "track_id")
    private Set<String> favorites = new LinkedHashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "owner_id")
    private List<PlaylistEntity> playlists = new ArrayList<>();

    protected MusicLibraryEntity() {
        // required by JPA
    }

    static MusicLibraryEntity fromDomain(MusicLibrary library) {
        MusicLibraryEntity entity = new MusicLibraryEntity();
        entity.ownerId = library.owner().value();
        entity.favorites = library.favorites().stream()
                .map(TrackId::value)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        entity.playlists = library.playlists().stream()
                .map(p -> new PlaylistEntity(
                        p.id().value(),
                        p.name(),
                        p.tracks().stream().map(TrackId::value).toList()))
                .collect(Collectors.toCollection(ArrayList::new));
        return entity;
    }

    MusicLibrary toDomain() {
        Set<TrackId> favoriteTracks = favorites.stream()
                .map(TrackId::of)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        List<Playlist> domainPlaylists = playlists.stream()
                .map(p -> Playlist.rehydrate(
                        PlaylistId.of(p.id()),
                        p.name(),
                        p.trackIds().stream().map(TrackId::of).toList()))
                .toList();
        return MusicLibrary.rehydrate(OwnerId.of(ownerId), favoriteTracks, domainPlaylists);
    }
}
