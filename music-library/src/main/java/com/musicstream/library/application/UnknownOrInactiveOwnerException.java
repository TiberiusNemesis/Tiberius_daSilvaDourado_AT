package com.musicstream.library.application;

import com.musicstream.shared.DomainException;

/**
 * Raised when a library operation is requested for a listener the
 * Anticorruption Layer cannot vouch for (unknown or not active).
 */
public class UnknownOrInactiveOwnerException extends DomainException {

    public UnknownOrInactiveOwnerException(String listenerId) {
        super("listener is unknown or not active: " + listenerId);
    }
}
