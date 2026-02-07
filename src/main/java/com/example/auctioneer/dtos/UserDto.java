package com.example.auctioneer.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserDto {
    private String username;
    private List<String> roles;
}
