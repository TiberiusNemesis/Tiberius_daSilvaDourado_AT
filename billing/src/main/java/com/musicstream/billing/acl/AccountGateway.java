package com.musicstream.billing.acl;

import java.util.Optional;

/**
 * Anticorruption Layer port towards the Listener Account context. Billing asks
 * "is this a real, active customer?" and receives a {@link BillingCustomer},
 * never the upstream account model. The translating adapter lives in the
 * bootstrap module.
 */
public interface AccountGateway {

    Optional<BillingCustomer> findCustomer(String listenerId);
}
