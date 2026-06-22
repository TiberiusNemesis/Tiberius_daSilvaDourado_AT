package com.musicstream.billing.domain.service.rules;

import com.musicstream.billing.domain.model.Subscriber;
import com.musicstream.billing.domain.service.AuthorizationRule;
import com.musicstream.billing.domain.service.RejectionReason;
import com.musicstream.billing.domain.service.TransactionRequest;

import java.time.Duration;
import java.util.Optional;

/**
 * "No more than 3 transactions within a 2-minute interval." If three
 * transactions have already been authorized in the two minutes leading up to
 * the request, a fourth is refused.
 */
public class HighFrequencySmallIntervalRule implements AuthorizationRule {

    private static final Duration WINDOW = Duration.ofMinutes(2);
    private static final int MAX_IN_WINDOW = 3;

    @Override
    public Optional<RejectionReason> evaluate(Subscriber subscriber, TransactionRequest request) {
        long recent = subscriber.transactionsWithin(WINDOW, request.occurredAt()).size();
        if (recent >= MAX_IN_WINDOW) {
            return Optional.of(RejectionReason.HIGH_FREQUENCY_SMALL_INTERVAL);
        }
        return Optional.empty();
    }
}
