package com.musicstream.billing.domain.service.rules;

import com.musicstream.billing.domain.model.Subscriber;
import com.musicstream.billing.domain.service.AuthorizationRule;
import com.musicstream.billing.domain.service.RejectionReason;
import com.musicstream.billing.domain.service.TransactionRequest;

import java.time.Duration;
import java.util.Optional;

/**
 * "No more than 2 similar transactions (same amount and merchant) within a
 * 2-minute interval." If two such transactions are already on record in the
 * window, a third is refused as a duplicate.
 */
public class DuplicatedTransactionRule implements AuthorizationRule {

    private static final Duration WINDOW = Duration.ofMinutes(2);
    private static final int MAX_SIMILAR_IN_WINDOW = 2;

    @Override
    public Optional<RejectionReason> evaluate(Subscriber subscriber, TransactionRequest request) {
        long similar = subscriber.countSimilarWithin(
                request.amount(), request.merchant(), WINDOW, request.occurredAt());
        if (similar >= MAX_SIMILAR_IN_WINDOW) {
            return Optional.of(RejectionReason.DUPLICATED_TRANSACTION);
        }
        return Optional.empty();
    }
}
