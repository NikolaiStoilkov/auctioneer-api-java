package com.auctioneer.exceptions;

/** Thrown when no user exists for a given id or username; mapped to HTTP 404. */
public class UserNotFoundException extends ResourceNotFoundException {

    public UserNotFoundException(Long userId) {
        super("User not found: " + userId);
    }

    public UserNotFoundException(String username) {
        super("User not found: " + username);
    }
}
