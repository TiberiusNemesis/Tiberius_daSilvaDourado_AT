package com.musicstream.billing.application;

import com.musicstream.shared.DomainException;

/** Raised when billing is asked to act for a listener the ACL cannot confirm as active. */
public class UnknownOrInactiveCustomerException extends DomainException {

    public UnknownOrInactiveCustomerException(String listenerId) {
        super("listener is unknown or not active: " + listenerId);
    }
}
