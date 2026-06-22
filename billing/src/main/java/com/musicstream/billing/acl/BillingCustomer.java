package com.musicstream.billing.acl;

import com.musicstream.billing.domain.model.SubscriberId;

/**
 * Billing's own view of a listener: just the identity and whether they are an
 * active account. The Anticorruption Layer translates the upstream account into
 * this shape so the core model never depends on the account context.
 */
public record BillingCustomer(SubscriberId id, boolean active) {

    public BillingCustomer {
        if (id == null) {
            throw new IllegalArgumentException("subscriber id must not be null");
        }
    }
}
