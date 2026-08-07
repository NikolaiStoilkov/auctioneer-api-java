package com.auctioneer.exceptions;

/**
 * Base class for all "resource not found" errors. Mapped to HTTP 404.
 */
public abstract class ResourceNotFoundException extends RuntimeException {

    protected ResourceNotFoundException(String message) {
        super(message);
    }
}
