package com.musicstream.library.acl;

import java.util.Optional;

/**
 * Anticorruption Layer port. The Music Library asks "who is this owner, in MY
 * terms?" and gets back a {@link LibraryOwner} — never the upstream account
 * model. The adapter that talks to the Listener Account context and performs
 * the translation lives in the bootstrap module, so this context stays
 * decoupled from its supplier (Customer-Supplier relationship).
 */
public interface AccountDirectory {

    Optional<LibraryOwner> findOwner(String listenerId);
}
