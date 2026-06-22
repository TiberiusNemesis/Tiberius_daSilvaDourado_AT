package com.musicstream.billing.domain.model;

import java.time.YearMonth;
import java.util.Objects;

/**
 * Credit card Value Object. It knows two distinct things the business cares
 * about and keeps them separate:
 *
 * <ul>
 *   <li>{@link #isValid()} — the card is structurally sound (Luhn) and not
 *       expired. This backs the rule "the user must have a valid credit card".</li>
 *   <li>{@link #isActive()} — the card is currently enabled for charging. This
 *       backs the rule "no transaction is accepted when the card is not active".</li>
 * </ul>
 */
public final class CreditCard {

    private final String number;
    private final String holderName;
    private final YearMonth expiry;
    private final CardStatus status;

    private CreditCard(String number, String holderName, YearMonth expiry, CardStatus status) {
        this.number = number;
        this.holderName = holderName;
        this.expiry = expiry;
        this.status = status;
    }

    public static CreditCard of(String number, String holderName, YearMonth expiry, CardStatus status) {
        String digits = number == null ? "" : number.replaceAll("\\s", "");
        if (holderName == null || holderName.isBlank()) {
            throw new IllegalArgumentException("card holder must not be blank");
        }
        if (expiry == null) {
            throw new IllegalArgumentException("card expiry must not be null");
        }
        return new CreditCard(digits, holderName.trim(), expiry, status);
    }

    /** Structural validity: passes the Luhn checksum and is not expired. */
    public boolean isValid() {
        return hasPlausibleLength() && passesLuhn() && !isExpired();
    }

    public boolean isActive() {
        return status == CardStatus.ACTIVE;
    }

    public boolean isExpired() {
        return expiry.isBefore(YearMonth.now());
    }

    public String masked() {
        if (number.length() < 4) {
            return "****";
        }
        return "**** **** **** " + number.substring(number.length() - 4);
    }

    private boolean hasPlausibleLength() {
        return number.length() >= 13 && number.length() <= 19 && number.chars().allMatch(Character::isDigit);
    }

    private boolean passesLuhn() {
        int sum = 0;
        boolean doubleDigit = false;
        for (int i = number.length() - 1; i >= 0; i--) {
            int digit = number.charAt(i) - '0';
            if (doubleDigit) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum += digit;
            doubleDigit = !doubleDigit;
        }
        return sum % 10 == 0;
    }

    public String number() {
        return number;
    }

    public String holderName() {
        return holderName;
    }

    public YearMonth expiry() {
        return expiry;
    }

    public CardStatus status() {
        return status;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj instanceof CreditCard other
                && number.equals(other.number)
                && expiry.equals(other.expiry)
                && status == other.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, expiry, status);
    }
}
