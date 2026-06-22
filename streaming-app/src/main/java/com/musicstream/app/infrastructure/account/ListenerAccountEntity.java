package com.musicstream.app.infrastructure.account;

import com.musicstream.account.domain.model.AccountStatus;
import com.musicstream.account.domain.model.Email;
import com.musicstream.account.domain.model.ListenerAccount;
import com.musicstream.account.domain.model.ListenerId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * JPA persistence model for a listener account. Kept deliberately separate from
 * the {@link ListenerAccount} aggregate so persistence concerns never leak into
 * the domain. Translation happens here, in the infrastructure layer.
 */
@Entity
@Table(name = "listener_account")
class ListenerAccountEntity {

    @Id
    private String id;
    private String name;

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    protected ListenerAccountEntity() {
        // required by JPA
    }

    static ListenerAccountEntity fromDomain(ListenerAccount account) {
        ListenerAccountEntity entity = new ListenerAccountEntity();
        entity.id = account.id().value();
        entity.name = account.name();
        entity.email = account.email().value();
        entity.status = account.status();
        return entity;
    }

    ListenerAccount toDomain() {
        return ListenerAccount.rehydrate(ListenerId.of(id), name, Email.of(email), status);
    }

    String email() {
        return email;
    }
}
