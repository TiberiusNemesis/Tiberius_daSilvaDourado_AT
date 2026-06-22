package com.musicstream.app.infrastructure.library;

import com.musicstream.account.domain.model.ListenerId;
import com.musicstream.account.domain.repository.ListenerAccountRepository;
import com.musicstream.library.acl.AccountDirectory;
import com.musicstream.library.acl.LibraryOwner;
import com.musicstream.library.domain.model.OwnerId;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Anticorruption Layer adapter for the Music Library. It reaches into the
 * Listener Account context and translates a {@code ListenerAccount} into the
 * library's own {@link LibraryOwner}, so the supplier's model never crosses the
 * boundary.
 */
@Component
class ListenerAccountDirectory implements AccountDirectory {

    private final ListenerAccountRepository accounts;

    ListenerAccountDirectory(ListenerAccountRepository accounts) {
        this.accounts = accounts;
    }

    @Override
    public Optional<LibraryOwner> findOwner(String listenerId) {
        if (listenerId == null || listenerId.isBlank()) {
            return Optional.empty();
        }
        return accounts.findById(ListenerId.of(listenerId))
                .map(account -> new LibraryOwner(OwnerId.of(account.id().value()), account.isActive()));
    }
}
