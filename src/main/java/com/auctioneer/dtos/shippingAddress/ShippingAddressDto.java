package com.auctioneer.dtos.shippingAddress;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/** API representation of a user's shipping address. */
public class ShippingAddressDto {
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @Size(max = 50, message = "Middle name must be at most 50 characters")
    private String middleName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Phone number must be valid")
    private String phoneNumber;

    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Street is required")
    private String street;

    @NotBlank(message = "Street number is required")
    private String streetNumber;

    @NotBlank(message = "Postal code is required")
    @Pattern(regexp = "^[0-9]{4,10}$", message = "Postal code must be valid")
    private String postalCode;
}
