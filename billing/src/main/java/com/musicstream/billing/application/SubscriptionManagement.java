package com.musicstream.billing.application;

import com.musicstream.billing.domain.model.Plan;
import com.musicstream.billing.domain.model.Subscription;

import java.time.YearMonth;

/**
 * Intention-revealing interface for the subscription use cases: register the
 * means of payment, then take up a plan.
 */
public interface SubscriptionManagement {

    void registerCreditCard(String listenerId, String cardNumber, String holderName,
                            YearMonth expiry, boolean active);

    Subscription subscribe(String listenerId, Plan plan);
}
