package com.auctioneer.dtos.auth;

import com.auctioneer.dtos.user.UserDto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthDto {
    private UserDto user;
    private String token;
}
