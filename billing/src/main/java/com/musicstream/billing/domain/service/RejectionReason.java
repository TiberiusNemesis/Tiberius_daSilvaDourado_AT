package com.musicstream.billing.domain.service;

/**
 * The business vocabulary for why a transaction is refused. The {@code code} is
 * the exact term defined by the platform's Ubiquitous Language (kept in
 * Portuguese, as specified) and is what the API returns to clients.
 */
public enum RejectionReason {

    CARD_NOT_ACTIVE("cartao-nao-ativo"),
    HIGH_FREQUENCY_SMALL_INTERVAL("alta-frequencia-pequeno-intervalo"),
    DUPLICATED_TRANSACTION("transacao-duplicada");

    private final String code;

    RejectionReason(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}
