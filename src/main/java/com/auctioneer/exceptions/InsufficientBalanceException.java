package com.auctioneer.exceptions;

/**
 * Thrown when a wallet operation requires more balance than available. Mapped to HTTP 400.
 */
public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException() {
        super("Insufficient balance to place bid");
    }
}
