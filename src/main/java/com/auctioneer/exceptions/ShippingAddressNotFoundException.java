package com.auctioneer.exceptions;

public class ShippingAddressNotFoundException extends ResourceNotFoundException {

    public ShippingAddressNotFoundException(Long id) {
        super("Shipping address not found: " + id);
    }
}
