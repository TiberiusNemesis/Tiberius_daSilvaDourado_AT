package com.musicstream.library.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Aggregate Root of the Music Library context: one library per owner, holding
 * their favorite tracks and their playlists. All changes go through this root,
 * which keeps the playlists and favorites consistent.
 */
public class MusicLibrary {

    private final OwnerId owner;
    private final Set<TrackId> favorites;
    private final List<Playlist> playlists;

    private MusicLibrary(OwnerId owner, Set<TrackId> favorites, List<Playlist> playlists) {
        this.owner = owner;
        this.favorites = favorites;
        this.playlists = playlists;
    }

    /** Factory: an empty library for a freshly validated owner. */
    public static MusicLibrary openFor(OwnerId owner) {
        return new MusicLibrary(owner, new LinkedHashSet<>(), new ArrayList<>());
    }

    public static MusicLibrary rehydrate(OwnerId owner, Set<TrackId> favorites, List<Playlist> playlists) {
        return new MusicLibrary(owner, new LinkedHashSet<>(favorites), new ArrayList<>(playlists));
    }

    /** Favoriting is idempotent: marking the same track twice is harmless. */
    public void favorite(TrackId track) {
        favorites.add(track);
    }

    public void unfavorite(TrackId track) {
        favorites.remove(track);
    }

    public PlaylistId createPlaylist(String name) {
        Playlist playlist = Playlist.create(name);
        playlists.add(playlist);
        return playlist.id();
    }

    public void addTrackToPlaylist(PlaylistId playlistId, TrackId track) {
        Playlist playlist = playlistById(playlistId)
                .orElseThrow(() -> new PlaylistNotFoundException(playlistId));
        playlist.add(track);
    }

    private Optional<Playlist> playlistById(PlaylistId playlistId) {
        return playlists.stream().filter(p -> p.id().equals(playlistId)).findFirst();
    }

    public OwnerId owner() {
        return owner;
    }

    public Set<TrackId> favorites() {
        return Collections.unmodifiableSet(favorites);
    }

    public List<Playlist> playlists() {
        return Collections.unmodifiableList(playlists);
    }
}
