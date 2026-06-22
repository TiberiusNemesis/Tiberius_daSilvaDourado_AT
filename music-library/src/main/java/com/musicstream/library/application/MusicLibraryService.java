package com.musicstream.library.application;

import com.musicstream.library.acl.AccountDirectory;
import com.musicstream.library.acl.LibraryOwner;
import com.musicstream.library.domain.model.MusicLibrary;
import com.musicstream.library.domain.model.OwnerId;
import com.musicstream.library.domain.model.PlaylistId;
import com.musicstream.library.domain.model.TrackId;
import com.musicstream.library.domain.repository.MusicLibraryRepository;

/**
 * Application Service for the Music Library. Before any change it consults the
 * Anticorruption Layer to make sure the listener is a valid, active owner, then
 * loads (or opens) the library aggregate and lets it perform the change.
 */
public class MusicLibraryService implements MusicLibraryManagement {

    private final MusicLibraryRepository libraries;
    private final AccountDirectory accountDirectory;

    public MusicLibraryService(MusicLibraryRepository libraries, AccountDirectory accountDirectory) {
        this.libraries = libraries;
        this.accountDirectory = accountDirectory;
    }

    @Override
    public void favoriteTrack(String listenerId, String trackId) {
        MusicLibrary library = libraryFor(listenerId);
        library.favorite(TrackId.of(trackId));
        libraries.save(library);
    }

    @Override
    public PlaylistId createPlaylist(String listenerId, String playlistName) {
        MusicLibrary library = libraryFor(listenerId);
        PlaylistId playlistId = library.createPlaylist(playlistName);
        libraries.save(library);
        return playlistId;
    }

    @Override
    public void addTrackToPlaylist(String listenerId, String playlistId, String trackId) {
        MusicLibrary library = libraryFor(listenerId);
        library.addTrackToPlaylist(PlaylistId.of(playlistId), TrackId.of(trackId));
        libraries.save(library);
    }

    /** Validate the owner through the ACL, then load or open their library. */
    private MusicLibrary libraryFor(String listenerId) {
        LibraryOwner owner = accountDirectory.findOwner(listenerId)
                .filter(LibraryOwner::active)
                .orElseThrow(() -> new UnknownOrInactiveOwnerException(listenerId));
        OwnerId ownerId = owner.id();
        return libraries.findByOwner(ownerId).orElseGet(() -> MusicLibrary.openFor(ownerId));
    }
}
