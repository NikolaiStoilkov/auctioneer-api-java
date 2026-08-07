package com.auctioneer.exceptions;

/**
 * Thrown on sign-up when the email is taken. Mapped to HTTP 409.
 */
public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException() {
        super("Email already exists");
    }
}
