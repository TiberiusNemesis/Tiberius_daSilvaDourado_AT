package com.musicstream.app.infrastructure.library;

import com.musicstream.library.domain.model.MusicLibrary;
import com.musicstream.library.domain.model.OwnerId;
import com.musicstream.library.domain.repository.MusicLibraryRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * H2/JPA adapter for the {@link MusicLibraryRepository} port. Transactional so
 * the mapping that reads the lazy favorites and playlist collections runs inside
 * an open session.
 */
@Repository
@Transactional
class JpaMusicLibraryRepository implements MusicLibraryRepository {

    private final SpringDataMusicLibraryRepository jpa;

    JpaMusicLibraryRepository(SpringDataMusicLibraryRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public MusicLibrary save(MusicLibrary library) {
        jpa.save(MusicLibraryEntity.fromDomain(library));
        return library;
    }

    @Override
    public Optional<MusicLibrary> findByOwner(OwnerId owner) {
        return jpa.findById(owner.value()).map(MusicLibraryEntity::toDomain);
    }
}
