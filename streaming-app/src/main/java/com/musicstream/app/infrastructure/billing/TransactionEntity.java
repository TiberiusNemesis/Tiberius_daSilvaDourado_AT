package com.musicstream.app.infrastructure.billing;

import com.musicstream.billing.domain.model.Merchant;
import com.musicstream.billing.domain.model.Transaction;
import com.musicstream.billing.domain.model.TransactionId;
import com.musicstream.shared.Money;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;

/** JPA persistence model for an authorized transaction (child of the subscriber). */
@Entity
@Table(name = "authorized_transaction")
class TransactionEntity {

    @Id
    private String id;
    private BigDecimal amount;
    private String merchant;
    private Instant occurredAt;

    protected TransactionEntity() {
        // required by JPA
    }

    static TransactionEntity fromDomain(Transaction transaction) {
        TransactionEntity entity = new TransactionEntity();
        entity.id = transaction.id().value();
        entity.amount = transaction.amount().value();
        entity.merchant = transaction.merchant().name();
        entity.occurredAt = transaction.occurredAt();
        return entity;
    }

    Transaction toDomain() {
        return Transaction.rehydrate(
                TransactionId.of(id), Money.of(amount), Merchant.of(merchant), occurredAt);
    }
}
