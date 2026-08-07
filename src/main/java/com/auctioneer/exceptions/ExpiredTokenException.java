package com.auctioneer.exceptions;

/**
 * Thrown when a JWT is expired. Mapped to HTTP 401.
 */
public class ExpiredTokenException extends RuntimeException {

    public ExpiredTokenException() {
        super("Token expired");
    }
}
