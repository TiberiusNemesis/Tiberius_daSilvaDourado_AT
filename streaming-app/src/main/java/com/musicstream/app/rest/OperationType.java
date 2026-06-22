package com.musicstream.app.rest;

import java.util.Arrays;

/**
 * The kinds of operation a processed line can represent. The {@code wire} value
 * is the discriminator clients send in the {@code type} field, decoupling the
 * public contract from the enum names.
 */
public enum OperationType {

    ACCOUNT("account"),
    CREDIT_CARD("credit-card"),
    SUBSCRIPTION("subscription"),
    FAVORITE("favorite"),
    PLAYLIST("playlist"),
    ADD_TO_PLAYLIST("add-to-playlist"),
    AUTHORIZE_TRANSACTION("authorize-transaction");

    private final String wire;

    OperationType(String wire) {
        this.wire = wire;
    }

    public String wire() {
        return wire;
    }

    public static OperationType fromWire(String value) {
        return Arrays.stream(values())
                .filter(op -> op.wire.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("unknown operation type: " + value));
    }
}
