package com.musicstream.billing.domain.repository;

import com.musicstream.billing.domain.model.Subscriber;
import com.musicstream.billing.domain.model.SubscriberId;

import java.util.Optional;

/** Repository (port) for the {@link Subscriber} aggregate. */
public interface SubscriberRepository {

    Subscriber save(Subscriber subscriber);

    Optional<Subscriber> findById(SubscriberId id);
}
