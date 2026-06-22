package com.musicstream.app.infrastructure.billing;

import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data JPA repository over the {@link SubscriberEntity} table. */
interface SpringDataSubscriberRepository extends JpaRepository<SubscriberEntity, String> {
}
