package com.musicstream.billing.domain.service;

import com.musicstream.billing.domain.model.Subscriber;
import com.musicstream.billing.domain.model.Transaction;

import java.util.List;

/**
 * Domain Service that authorizes transactions against the anti-fraud policy.
 *
 * It owns no state of its own: it coordinates the {@link Subscriber} aggregate
 * and the configured set of {@link AuthorizationRule}s. Because the rules are
 * injected, the policy grows by adding rules — the service stays untouched
 * (Open/Closed). On approval it asks the subscriber to record the transaction,
 * keeping the state change inside the aggregate.
 */
public class TransactionAuthorizationService {

    private final List<AuthorizationRule> rules;

    public TransactionAuthorizationService(List<AuthorizationRule> rules) {
        this.rules = List.copyOf(rules);
    }

    public AuthorizationResult authorize(Subscriber subscriber, TransactionRequest request) {
        List<RejectionReason> violations = rules.stream()
                .map(rule -> rule.evaluate(subscriber, request))
                .flatMap(java.util.Optional::stream)
                .toList();

        if (!violations.isEmpty()) {
            return AuthorizationResult.rejected(violations);
        }

        Transaction authorized = subscriber.recordAuthorized(
                request.amount(), request.merchant(), request.occurredAt());
        return AuthorizationResult.approved(authorized.id());
    }
}
