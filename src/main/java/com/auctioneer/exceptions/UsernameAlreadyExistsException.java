package com.auctioneer.exceptions;

/**
 * Thrown on sign-up when the username is taken. Mapped to HTTP 409.
 */
public class UsernameAlreadyExistsException extends RuntimeException {

    public UsernameAlreadyExistsException() {
        super("Username already exists");
    }
}
