package com.auctioneer.exceptions;

public class AdNotFoundException extends ResourceNotFoundException {

    public AdNotFoundException(Long adId) {
        super("Ad not found: " + adId);
    }
}
