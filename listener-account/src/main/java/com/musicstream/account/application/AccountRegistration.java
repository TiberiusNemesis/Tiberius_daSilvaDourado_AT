package com.musicstream.account.application;

import com.musicstream.account.domain.model.Email;
import com.musicstream.account.domain.model.ListenerAccount;

/**
 * Intention-revealing interface (Evans) for the single use case this context
 * offers to the outside world: registering a listener. The name states the
 * intent; the signature states exactly what is needed and what comes back.
 */
public interface AccountRegistration {

    ListenerAccount register(String name, Email email);
}
