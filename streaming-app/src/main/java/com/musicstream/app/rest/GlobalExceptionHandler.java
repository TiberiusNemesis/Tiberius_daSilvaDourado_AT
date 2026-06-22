package com.musicstream.app.rest;

import com.musicstream.shared.DomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Translates domain and validation errors into uniform HTTP responses, so the
 * contexts can keep throwing meaningful exceptions without knowing about HTTP.
 */
@RestControllerAdvice
class GlobalExceptionHandler {

    /** A broken business rule is a well-formed request the domain refuses: 422. */
    @ExceptionHandler(DomainException.class)
    ResponseEntity<Map<String, Object>> handleDomain(DomainException ex) {
        return ResponseEntity.unprocessableEntity()
                .body(Map.of("status", "rejected", "message", ex.getMessage()));
    }

    /** Malformed input (bad enum, blank field, bad date): 400. */
    @ExceptionHandler({IllegalArgumentException.class, java.time.format.DateTimeParseException.class})
    ResponseEntity<Map<String, Object>> handleBadInput(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("status", "error", "message", ex.getMessage()));
    }
}
