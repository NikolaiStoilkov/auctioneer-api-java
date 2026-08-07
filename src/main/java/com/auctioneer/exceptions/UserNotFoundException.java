package com.auctioneer.exceptions;

public class UserNotFoundException extends ResourceNotFoundException {

    public UserNotFoundException(Long userId) {
        super("User not found: " + userId);
    }

    public UserNotFoundException(String username) {
        super("User not found: " + username);
    }
}
