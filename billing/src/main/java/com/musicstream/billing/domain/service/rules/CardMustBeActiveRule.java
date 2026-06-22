package com.musicstream.billing.domain.service.rules;

import com.musicstream.billing.domain.model.Subscriber;
import com.musicstream.billing.domain.service.AuthorizationRule;
import com.musicstream.billing.domain.service.RejectionReason;
import com.musicstream.billing.domain.service.TransactionRequest;

import java.util.Optional;

/**
 * "No transaction is accepted when the card is not active." A subscriber with no
 * card, or with an inactive one, fails this rule.
 */
public class CardMustBeActiveRule implements AuthorizationRule {

    @Override
    public Optional<RejectionReason> evaluate(Subscriber subscriber, TransactionRequest request) {
        if (subscriber.hasActiveCard()) {
            return Optional.empty();
        }
        return Optional.of(RejectionReason.CARD_NOT_ACTIVE);
    }
}
