package com.auctioneer.controller.auth;

import com.auctioneer.dtos.forms.UserAuthSignInDto;
import com.auctioneer.dtos.forms.UserAuthSignUpDto;

import com.auctioneer.service.user.UserAuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserAuthService userAuthService;

    /**
     * Registers a new user account.
     *
     * @param userAuthSignUpDto the sign-up credentials
     * @return a JWT for the newly created user
     */
    @PostMapping("/sign-up")
    public String signUp(@Valid @RequestBody UserAuthSignUpDto userAuthSignUpDto) {
        return userAuthService.signUp(userAuthSignUpDto);
    }

    /**
     * Authenticates an existing user.
     *
     * @param userAuthSignInDto the sign-in credentials
     * @return a JWT for the authenticated user
     */
    @PostMapping("/sign-in")
    public String signIn(@Valid @RequestBody UserAuthSignInDto userAuthSignInDto) {
        return userAuthService.signIn(userAuthSignInDto);
    }
}
