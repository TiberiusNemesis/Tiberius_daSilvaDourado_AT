package com.musicstream.app.rest.dto;

import java.util.Map;

/**
 * Uniform answer for any operation: which operation ran, its outcome, and any
 * data it produced (e.g. a new {@code listenerId}, a {@code playlistId}, or the
 * anti-fraud {@code violations}).
 */
public record OperationResponse(String operation, String status, Map<String, Object> data) {

    public static OperationResponse ok(String operation, Map<String, Object> data) {
        return new OperationResponse(operation, "ok", data);
    }

    public static OperationResponse of(String operation, String status, Map<String, Object> data) {
        return new OperationResponse(operation, status, data);
    }
}
