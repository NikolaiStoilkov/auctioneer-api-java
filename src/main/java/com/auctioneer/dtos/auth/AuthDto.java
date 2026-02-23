package com.auctioneer.dtos.auth;

import com.auctioneer.domain.entities.User;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthDto {
    private User user;
    private String token;
}
