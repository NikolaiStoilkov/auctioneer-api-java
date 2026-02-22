package com.auctioneer.controller;

import com.auctioneer.domain.entities.User;
import com.auctioneer.dtos.AuthDto;
import com.auctioneer.dtos.forms.UserAuthSignInDto;
import com.auctioneer.dtos.forms.UserAuthSignUpDto;
import com.auctioneer.service.AuthenticationService;
import com.auctioneer.service.JwtService;

import jakarta.validation.Valid;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final ApplicationEventPublisher eventPublisher;
    private final JwtService jwtService;

    @GetMapping("/sign-up")
    private AuthDto signUp(@Valid @RequestBody UserAuthSignUpDto userAuthSignUpDto) {
        AuthDto authenticatedUser = authenticationService.create(userAuthSignUpDto);

//        if (!authenticatedUser.getUserDetails().isEnabled()) {
//            throw new RuntimeException("User account is disabled");
//        }

        User user = authenticatedUser.getUser();
//        eventPublisher.publishEvent(
//                new OnUserSignUpEvent(this, user)
//        );


        return authenticatedUser;
    }

    @GetMapping("/sign-in")
    private AuthDto signIn(HttpServletRequest request) {
        // TODO
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String jwt = authHeader
                .substring(7)
                .trim();

        UserAuthSignInDto parsedToken = jwtService.parseToken(jwt);

        String username = parsedToken.getUsername();

        AuthDto authenticatedUser = authenticationService.verify(jwt, username);

        return authenticatedUser;
    }
}
