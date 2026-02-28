package com.auctioneer.service.auth;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtService jwtService;

    public String initialize(Long userId, Map<String, Object> claims) {
        return jwtService.generateToken(
                userId,
                claims
        );
    }

    public String authorize(Long userId, Boolean isPasswordMatched,Map<String, Object> claims) {
        return jwtService.authorize(
                userId,
                isPasswordMatched,
                claims
        );
    }

    public Boolean verify(String token) {
        return jwtService.verify(token);
    }
}
