package com.musicstream.app.infrastructure.billing;

import com.musicstream.account.domain.model.ListenerId;
import com.musicstream.account.domain.repository.ListenerAccountRepository;
import com.musicstream.billing.acl.AccountGateway;
import com.musicstream.billing.acl.BillingCustomer;
import com.musicstream.billing.domain.model.SubscriberId;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Anticorruption Layer adapter for the Core billing context. Translates a
 * {@code ListenerAccount} into billing's own {@link BillingCustomer}, keeping
 * the Partnership decoupled at the model level.
 */
@Component
class ListenerAccountGateway implements AccountGateway {

    private final ListenerAccountRepository accounts;

    ListenerAccountGateway(ListenerAccountRepository accounts) {
        this.accounts = accounts;
    }

    @Override
    public Optional<BillingCustomer> findCustomer(String listenerId) {
        if (listenerId == null || listenerId.isBlank()) {
            return Optional.empty();
        }
        return accounts.findById(ListenerId.of(listenerId))
                .map(account -> new BillingCustomer(SubscriberId.of(account.id().value()), account.isActive()));
    }
}
