package com.auctioneer.exceptions;

/** Thrown when no image exists for a given code; mapped to HTTP 404. */
public class ImageNotFoundException extends ResourceNotFoundException {

    public ImageNotFoundException(String code) {
        super("Image not found: " + code);
    }
}
