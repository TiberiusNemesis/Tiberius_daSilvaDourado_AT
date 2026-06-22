package com.musicstream.app.rest.dto;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * A single processed line. The {@code type} decides which operation it is; the
 * remaining fields are those that operation needs (the rest stay null). Keeping
 * one flat envelope mirrors the "decide the operation from the line" brief while
 * staying trivial to post as JSON.
 */
public record OperationRequest(
        String type,
        // account
        String name,
        String email,
        // the listener every later operation refers to
        String listenerId,
        // subscription
        String plan,
        // credit card
        String cardNumber,
        String cardHolder,
        String cardExpiry,
        Boolean cardActive,
        // library
        String trackId,
        String playlistName,
        String playlistId,
        // transaction authorization
        BigDecimal amount,
        String merchant,
        Instant occurredAt) {
}
