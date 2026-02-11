package com.example.auctioneer.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JwtDto {
    private String username;
    private String passwordHash;
    private String token;
}
