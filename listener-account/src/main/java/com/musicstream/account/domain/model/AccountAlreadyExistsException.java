package com.musicstream.account.domain.model;

import com.musicstream.shared.DomainException;

/** Raised when registration is attempted for an email that is already in use. */
public class AccountAlreadyExistsException extends DomainException {

    public AccountAlreadyExistsException(Email email) {
        super("an account already exists for email " + email.value());
    }
}
