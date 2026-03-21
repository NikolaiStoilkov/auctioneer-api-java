package com.auctioneer.service.auth;

import io.jsonwebtoken.Claims;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    private Map<String, Object> claims;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        claims = Map.of("ROLES", List.of("USER"));
    }

    @Test
    void generateTokenShouldReturnNonNullToken() {
        String token = jwtService.generateToken(1L, claims);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void extractUserIdShouldReturnCorrectUserId() {
        String token = jwtService.generateToken(42L, claims);

        String userId = jwtService.extractUserId(token);

        assertEquals("42", userId);
    }

    @Test
    void extractExpirationShouldReturnFutureDate() {
        String token = jwtService.generateToken(1L, claims);

        Date expiration = jwtService.extractExpiration(token);

        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void extractAllClaimsShouldContainCustomClaims() {
        String token = jwtService.generateToken(1L, claims);

        Claims extractedClaims = jwtService.extractAllClaims(token);

        assertNotNull(extractedClaims);
        assertEquals("1", extractedClaims.getSubject());
        assertNotNull(extractedClaims.get("ROLES"));
    }

    @Test
    void isTokenExpiredShouldReturnFalseForFreshToken() {
        String token = jwtService.generateToken(1L, claims);

        Boolean expired = jwtService.isTokenExpired(token);

        assertFalse(expired);
    }

    @Test
    void authorizeShouldReturnTokenWhenPasswordMatches() {
        String token = jwtService.authorize(1L, true, claims);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void authorizeShouldThrowExceptionWhenPasswordDoesNotMatch() {
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> jwtService.authorize(1L, false, claims)
        );

        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    void verifyShouldReturnFalseForInvalidToken() {
        Boolean result = jwtService.verify("invalid.token.here");

        assertFalse(result);
    }

    @Test
    void verifyShouldHandleFreshToken() {
        String token = jwtService.generateToken(1L, claims);

        // verify() returns true if token is expired (before current date)
        // A fresh token should NOT be expired, so verify returns false
        Boolean result = jwtService.verify(token);

        assertFalse(result);
    }

    @Test
    void generateTokenShouldProduceDifferentTokensForDifferentUsers() {
        String token1 = jwtService.generateToken(1L, claims);
        String token2 = jwtService.generateToken(2L, claims);

        assertNotEquals(token1, token2);
    }

    @Test
    void extractClaimShouldExtractSubject() {
        String token = jwtService.generateToken(5L, claims);

        String subject = jwtService.extractClaim(token, Claims::getSubject);

        assertEquals("5", subject);
    }
}
