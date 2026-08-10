package com.auctioneer.exceptions;

/** Thrown when no shipping address exists for a given id; mapped to HTTP 404. */
public class ShippingAddressNotFoundException extends ResourceNotFoundException {

    public ShippingAddressNotFoundException(Long id) {
        super("Shipping address not found: " + id);
    }
}
