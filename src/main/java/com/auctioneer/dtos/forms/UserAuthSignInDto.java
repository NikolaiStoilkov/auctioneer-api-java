package com.auctioneer.dtos.forms;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAuthSignInDto {
    private String username;
    private String passwordHash;
}


