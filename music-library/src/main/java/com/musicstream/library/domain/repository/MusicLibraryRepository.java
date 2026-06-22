package com.musicstream.library.domain.repository;

import com.musicstream.library.domain.model.MusicLibrary;
import com.musicstream.library.domain.model.OwnerId;

import java.util.Optional;

/** Repository (port) for the {@link MusicLibrary} aggregate. */
public interface MusicLibraryRepository {

    MusicLibrary save(MusicLibrary library);

    Optional<MusicLibrary> findByOwner(OwnerId owner);
}
