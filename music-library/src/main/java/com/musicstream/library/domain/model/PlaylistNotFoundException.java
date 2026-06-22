package com.musicstream.library.domain.model;

import com.musicstream.shared.DomainException;

/** Raised when a track is added to a playlist that does not exist in the library. */
public class PlaylistNotFoundException extends DomainException {

    public PlaylistNotFoundException(PlaylistId playlistId) {
        super("playlist not found: " + playlistId.value());
    }
}
