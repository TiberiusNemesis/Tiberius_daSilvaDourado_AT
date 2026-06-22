package com.musicstream.library.acl;

import com.musicstream.library.domain.model.OwnerId;

/**
 * The Music Library's own notion of "a listener that is allowed to own a
 * library". It is the translated concept the Anticorruption Layer hands back,
 * deliberately exposing only what this context cares about (an id and whether
 * the owner is active) instead of the full upstream account model.
 */
public record LibraryOwner(OwnerId id, boolean active) {

    public LibraryOwner {
        if (id == null) {
            throw new IllegalArgumentException("owner id must not be null");
        }
    }
}
