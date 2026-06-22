package com.musicstream.shared;

/**
 * Base type for every error that represents a broken business rule (as opposed
 * to a technical failure). Bootstrap/interface layers can translate any
 * {@code DomainException} into a uniform client response.
 */
public abstract class DomainException extends RuntimeException {

    protected DomainException(String message) {
        super(message);
    }
}
