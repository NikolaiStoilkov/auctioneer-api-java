package com.auctioneer.exceptions;

public class ImageNotFoundException extends ResourceNotFoundException {

    public ImageNotFoundException(String code) {
        super("Image not found: " + code);
    }
}
