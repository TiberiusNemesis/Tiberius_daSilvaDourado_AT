package com.musicstream.billing.domain.service;

import com.musicstream.billing.domain.model.Merchant;
import com.musicstream.shared.Money;

import java.time.Instant;

/**
 * An attempt to authorize a payment: how much, to whom, and when. A Value
 * Object handed to the anti-fraud rules and, on approval, turned into a
 * recorded {@code Transaction}.
 */
public record TransactionRequest(Money amount, Merchant merchant, Instant occurredAt) {

    public TransactionRequest {
        if (amount == null || merchant == null || occurredAt == null) {
            throw new IllegalArgumentException("amount, merchant and occurredAt are required");
        }
    }
}
