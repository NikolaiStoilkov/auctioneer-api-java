package com.auctioneer.exceptions;

/** Thrown when no ad exists for a given id; mapped to HTTP 404. */
public class AdNotFoundException extends ResourceNotFoundException {

    public AdNotFoundException(Long adId) {
        super("Ad not found: " + adId);
    }
}
