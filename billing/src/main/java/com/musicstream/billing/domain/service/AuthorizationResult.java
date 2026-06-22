package com.musicstream.billing.domain.service;

import com.musicstream.billing.domain.model.TransactionId;

import java.util.List;
import java.util.Optional;

/**
 * Outcome of an authorization attempt. Mirrors the anti-fraud authorizer style:
 * the transaction is approved only when no rule was violated; otherwise it
 * reports every reason it was refused (kept as the Ubiquitous Language codes).
 */
public final class AuthorizationResult {

    private final TransactionId transactionId;
    private final List<RejectionReason> violations;

    private AuthorizationResult(TransactionId transactionId, List<RejectionReason> violations) {
        this.transactionId = transactionId;
        this.violations = List.copyOf(violations);
    }

    public static AuthorizationResult approved(TransactionId transactionId) {
        return new AuthorizationResult(transactionId, List.of());
    }

    public static AuthorizationResult rejected(List<RejectionReason> violations) {
        return new AuthorizationResult(null, violations);
    }

    public boolean isApproved() {
        return violations.isEmpty();
    }

    public Optional<TransactionId> transactionId() {
        return Optional.ofNullable(transactionId);
    }

    public List<RejectionReason> violations() {
        return violations;
    }
}
