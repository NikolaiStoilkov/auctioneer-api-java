package com.auctioneer.domain.entities;

/**
 * Lifecycle state of an ad: {@code ACTIVE} (open for bids),
 * {@code INACTIVE} (temporarily hidden), {@code SOLD}, or {@code CLOSED}
 * (past its end date).
 */
public enum Status {
    ACTIVE,
    INACTIVE,
    SOLD,
    CLOSED
}
