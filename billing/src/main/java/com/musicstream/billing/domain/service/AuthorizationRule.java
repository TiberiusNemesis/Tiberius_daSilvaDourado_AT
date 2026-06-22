package com.musicstream.billing.domain.service;

import com.musicstream.billing.domain.model.Subscriber;

import java.util.Optional;

/**
 * A single anti-fraud rule, expressed as a Specification. Each rule inspects the
 * subscriber and the incoming request and either approves (empty) or returns the
 * {@link RejectionReason} it stands for.
 *
 * This is the seam that keeps the design open for extension and closed for
 * modification (OCP): "new rules will appear in the future" simply means adding
 * a new implementation and registering it — no existing rule or the service
 * itself ever changes.
 */
public interface AuthorizationRule {

    Optional<RejectionReason> evaluate(Subscriber subscriber, TransactionRequest request);
}
