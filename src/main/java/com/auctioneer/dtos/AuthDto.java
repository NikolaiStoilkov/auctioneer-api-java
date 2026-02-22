package com.auctioneer.dtos;

import com.auctioneer.domain.entities.User;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthDto {
//    private UserDetails userDetails;
    private User user;
    private String token;
}
