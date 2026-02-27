package com.auctioneer.dtos.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
public class UserDto {
    private String username;
    private String passwordHash;
    private List<String> roles;
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
