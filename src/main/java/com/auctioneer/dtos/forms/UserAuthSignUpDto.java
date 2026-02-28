package com.auctioneer.dtos.forms;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAuthSignUpDto {
    private String username;
    private String password;

    //User
    private String firstName;
    private String middleName;
    private String role;
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


