package com.auctioneer.dtos.shippingAddress;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShippingAddressDto {
    private String firstName;
    private String middleName;
    private String lastName;
    private String phoneNumber;
    private String country;
    private String city;
    private String street;
    private String streetNumber;
    private String postalCode;
}
