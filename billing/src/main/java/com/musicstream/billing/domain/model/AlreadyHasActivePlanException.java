package com.musicstream.billing.domain.model;

import com.musicstream.shared.DomainException;

/** Enforces the rule "a user may have only one active plan". */
public class AlreadyHasActivePlanException extends DomainException {

    public AlreadyHasActivePlanException(SubscriberId subscriberId) {
        super("subscriber already has an active plan: " + subscriberId.value());
    }
}
