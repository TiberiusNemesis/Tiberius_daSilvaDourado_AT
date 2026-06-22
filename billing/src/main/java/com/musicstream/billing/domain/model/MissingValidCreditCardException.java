package com.musicstream.billing.domain.model;

import com.musicstream.shared.DomainException;

/** Enforces the rule "the user must have a valid credit card" for paid plans. */
public class MissingValidCreditCardException extends DomainException {

    public MissingValidCreditCardException(SubscriberId subscriberId) {
        super("subscriber has no valid credit card on file: " + subscriberId.value());
    }
}
