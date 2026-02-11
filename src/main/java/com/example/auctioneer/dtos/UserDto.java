package com.example.auctioneer.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDto<User> {
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