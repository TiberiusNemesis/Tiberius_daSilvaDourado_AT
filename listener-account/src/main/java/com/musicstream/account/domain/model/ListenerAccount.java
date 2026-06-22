package com.musicstream.account.domain.model;

/**
 * Aggregate Root of the Listener Account context.
 *
 * It is a rich model: state changes go through intention-revealing behaviour
 * ({@link #suspend()}, {@link #reinstate()}) that protect the invariants,
 * rather than exposing setters. Construction happens only through the
 * {@link #register(String, Email)} factory, which guarantees a valid account.
 */
public class ListenerAccount {

    private final ListenerId id;
    private final String name;
    private final Email email;
    private AccountStatus status;

    private ListenerAccount(ListenerId id, String name, Email email, AccountStatus status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.status = status;
    }

    /** Factory: registers a brand-new, active account. */
    public static ListenerAccount register(String name, Email email) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("listener name must not be blank");
        }
        return new ListenerAccount(ListenerId.newId(), name.trim(), email, AccountStatus.ACTIVE);
    }

    /** Rebuilds an account from persisted state (used by the repository). */
    public static ListenerAccount rehydrate(ListenerId id, String name, Email email, AccountStatus status) {
        return new ListenerAccount(id, name, email, status);
    }

    public void suspend() {
        this.status = AccountStatus.SUSPENDED;
    }

    public void reinstate() {
        this.status = AccountStatus.ACTIVE;
    }

    public boolean isActive() {
        return status == AccountStatus.ACTIVE;
    }

    public ListenerId id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Email email() {
        return email;
    }

    public AccountStatus status() {
        return status;
    }
}
