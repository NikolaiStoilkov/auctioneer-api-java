package com.auctioneer.exceptions;

/**
 * Thrown when authentication fails. Mapped to HTTP 401.
 */
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("Invalid credentials");
    }
}
