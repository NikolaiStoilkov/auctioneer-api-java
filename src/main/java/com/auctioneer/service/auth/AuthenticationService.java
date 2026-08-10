package com.auctioneer.service.auth;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * Thin façade over {@link JwtService} for issuing and verifying tokens.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtService jwtService;

    /**
     * Issues a token for a freshly created user.
     *
     * @param userId the user id
     * @param claims the token claims
     * @return the signed token
     */
    public String initialize(Long userId, Map<String, Object> claims) {
        return jwtService.generateToken(
                userId,
                claims
        );
    }

    /**
     * Issues a token when the supplied password matched.
     *
     * @param userId            the user id
     * @param isPasswordMatched whether the password check passed
     * @param claims            the token claims
     * @return the signed token
     */
    public String authorize(Long userId, Boolean isPasswordMatched,Map<String, Object> claims) {
        return jwtService.authorize(
                userId,
                isPasswordMatched,
                claims
        );
    }

    /**
     * Verifies a token, returning true if it has expired.
     *
     * @param token the token to check
     * @return whether the token is expired
     */
    public Boolean verify(String token) {
        return jwtService.verify(token);
    }
}
