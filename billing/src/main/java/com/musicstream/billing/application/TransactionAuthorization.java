package com.musicstream.billing.application;

import com.musicstream.billing.domain.service.AuthorizationResult;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Intention-revealing interface for authorizing a payment against the
 * anti-fraud policy.
 */
public interface TransactionAuthorization {

    AuthorizationResult authorize(String listenerId, BigDecimal amount, String merchant, Instant occurredAt);
}
