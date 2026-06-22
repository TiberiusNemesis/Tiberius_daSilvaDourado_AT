package com.musicstream.app.infrastructure.billing;

import com.musicstream.billing.domain.model.Subscriber;
import com.musicstream.billing.domain.model.SubscriberId;
import com.musicstream.billing.domain.repository.SubscriberRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * H2/JPA adapter for the {@link SubscriberRepository} port. Methods are
 * transactional so the entity-to-aggregate mapping (which walks lazy
 * collections such as the transaction history) runs inside an open session.
 */
@Repository
@Transactional
class JpaSubscriberRepository implements SubscriberRepository {

    private final SpringDataSubscriberRepository jpa;

    JpaSubscriberRepository(SpringDataSubscriberRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Subscriber save(Subscriber subscriber) {
        jpa.save(SubscriberEntity.fromDomain(subscriber));
        return subscriber;
    }

    @Override
    public Optional<Subscriber> findById(SubscriberId id) {
        return jpa.findById(id.value()).map(SubscriberEntity::toDomain);
    }
}
