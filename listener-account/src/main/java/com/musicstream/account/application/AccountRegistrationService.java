package com.musicstream.account.application;

import com.musicstream.account.domain.model.AccountAlreadyExistsException;
import com.musicstream.account.domain.model.Email;
import com.musicstream.account.domain.model.ListenerAccount;
import com.musicstream.account.domain.repository.ListenerAccountRepository;

/**
 * Application Service: thin orchestration around the aggregate. It enforces the
 * cross-aggregate uniqueness rule (one account per email) and delegates every
 * other decision to the rich {@link ListenerAccount} model.
 */
public class AccountRegistrationService implements AccountRegistration {

    private final ListenerAccountRepository accounts;

    public AccountRegistrationService(ListenerAccountRepository accounts) {
        this.accounts = accounts;
    }

    @Override
    public ListenerAccount register(String name, Email email) {
        if (accounts.existsByEmail(email)) {
            throw new AccountAlreadyExistsException(email);
        }
        return accounts.save(ListenerAccount.register(name, email));
    }
}
