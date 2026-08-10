package com.auctioneer.dtos.forms;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/** Login form carrying a username and password. */
public class UserAuthSignInDto {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;
}


