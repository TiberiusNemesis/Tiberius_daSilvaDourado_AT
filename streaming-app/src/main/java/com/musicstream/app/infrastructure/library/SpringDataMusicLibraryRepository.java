package com.musicstream.app.infrastructure.library;

import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data JPA repository over the {@link MusicLibraryEntity} table. */
interface SpringDataMusicLibraryRepository extends JpaRepository<MusicLibraryEntity, String> {
}
