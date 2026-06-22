package com.musicstream.app.infrastructure.account;

import com.musicstream.account.domain.model.Email;
import com.musicstream.account.domain.model.ListenerAccount;
import com.musicstream.account.domain.model.ListenerId;
import com.musicstream.account.domain.repository.ListenerAccountRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Adapter that implements the domain {@link ListenerAccountRepository} port on
 * top of H2/JPA, translating between the aggregate and its persistence model.
 */
@Repository
@Transactional
class JpaListenerAccountRepository implements ListenerAccountRepository {

    private final SpringDataListenerAccountRepository jpa;

    JpaListenerAccountRepository(SpringDataListenerAccountRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public ListenerAccount save(ListenerAccount account) {
        jpa.save(ListenerAccountEntity.fromDomain(account));
        return account;
    }

    @Override
    public Optional<ListenerAccount> findById(ListenerId id) {
        return jpa.findById(id.value()).map(ListenerAccountEntity::toDomain);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpa.existsByEmail(email.value());
    }
}
