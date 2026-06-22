package com.musicstream.library.application;

import com.musicstream.library.domain.model.PlaylistId;

/**
 * Intention-revealing interface for the Music Library use cases. Every method
 * names a single user intent: favorite a track, start a playlist, grow a
 * playlist.
 */
public interface MusicLibraryManagement {

    void favoriteTrack(String listenerId, String trackId);

    PlaylistId createPlaylist(String listenerId, String playlistName);

    void addTrackToPlaylist(String listenerId, String playlistId, String trackId);
}
