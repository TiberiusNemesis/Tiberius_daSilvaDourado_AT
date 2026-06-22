package com.musicstream.app.infrastructure.account;

import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data JPA repository over the {@link ListenerAccountEntity} table. */
interface SpringDataListenerAccountRepository extends JpaRepository<ListenerAccountEntity, String> {

    boolean existsByEmail(String email);
}
