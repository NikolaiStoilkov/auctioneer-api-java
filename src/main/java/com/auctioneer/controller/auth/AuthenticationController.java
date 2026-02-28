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

    @GetMapping("/sign-up")
    private String signUp(@Valid @RequestBody UserAuthSignUpDto userAuthSignUpDto) {
        return userAuthService.signUp(userAuthSignUpDto);
    }

    @GetMapping("/sign-in")
    private String signIn(@Valid @RequestBody UserAuthSignInDto userAuthSignInDto) {
        return userAuthService.signIn(userAuthSignInDto);
    }
}
