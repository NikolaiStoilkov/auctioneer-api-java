package com.auctioneer.dtos.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class UserDto {
    private String username;
    private String passwordHash;
    private String role;
    private String firstName;
    private String middleName;
    private String lastName;
    private String ucn;
    private String country;
    private String city;
    private String street;
    private String streetNumber;
    private String postalCode;
    private String phoneNumber;
    private String email;
}
