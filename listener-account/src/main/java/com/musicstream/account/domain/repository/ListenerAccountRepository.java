package com.musicstream.account.domain.repository;

import com.musicstream.account.domain.model.Email;
import com.musicstream.account.domain.model.ListenerAccount;
import com.musicstream.account.domain.model.ListenerId;

import java.util.Optional;

/**
 * Repository (port) for the {@link ListenerAccount} aggregate. The domain
 * speaks only to this interface; the H2/JPA adapter that implements it lives in
 * the bootstrap module, keeping this context free of any framework.
 */
public interface ListenerAccountRepository {

    ListenerAccount save(ListenerAccount account);

    Optional<ListenerAccount> findById(ListenerId id);

    boolean existsByEmail(Email email);
}
