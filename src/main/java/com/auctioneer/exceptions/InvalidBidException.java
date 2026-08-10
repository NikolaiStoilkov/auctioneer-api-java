package com.auctioneer.exceptions;

/**
 * Thrown when a bid violates auction rules. Mapped to HTTP 400.
 */
public class InvalidBidException extends RuntimeException {

    private InvalidBidException(String message) {
        super(message);
    }

    public static InvalidBidException ownAdBid() {
        return new InvalidBidException("You cannot bid on your own ad");
    }

    public static InvalidBidException bidTooLow() {
        return new InvalidBidException("Bid amount must be higher than current bid price");
    }
}
