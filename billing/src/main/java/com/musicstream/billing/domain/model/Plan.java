package com.musicstream.billing.domain.model;

import com.musicstream.shared.Money;

/**
 * The subscription plans offered by the platform. Each plan knows its own
 * monthly price, so pricing logic stays in the domain rather than leaking into
 * application code.
 */
public enum Plan {

    FREE(Money.ZERO),
    PREMIUM(Money.of("19.90")),
    FAMILY(Money.of("34.90"));

    private final Money monthlyPrice;

    Plan(Money monthlyPrice) {
        this.monthlyPrice = monthlyPrice;
    }

    public Money monthlyPrice() {
        return monthlyPrice;
    }

    /** A paid plan requires a valid credit card on file before it can start. */
    public boolean isPaid() {
        return !monthlyPrice.isZero();
    }
}
