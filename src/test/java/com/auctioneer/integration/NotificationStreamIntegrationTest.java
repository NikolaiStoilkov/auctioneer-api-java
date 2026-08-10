package com.auctioneer.integration;

import com.auctioneer.service.auth.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NotificationStreamIntegrationTest extends IntegrationTestBase {

    @Test
    void notificationStream_withValidToken_opensSseConnection() throws Exception {
        String token = signUpUniqueUser();

        mockMvc.perform(get("/api/users/notifications/stream")
                        .param("token", token))
                .andExpect(status().isOk());
    }

    @Test
    void notificationStream_withGarbageToken_fails() throws Exception {
        mockMvc.perform(get("/api/users/notifications/stream")
                        .param("token", "not-a-jwt"))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void notificationStream_withExpiredToken_returnsUnauthorized() throws Exception {
        String expiredToken = buildExpiredToken(1L);

        mockMvc.perform(get("/api/users/notifications/stream")
                        .param("token", expiredToken))
                .andExpect(status().isUnauthorized());
    }

    /** Builds a correctly-signed JWT whose expiry is already in the past. */
    private String buildExpiredToken(Long userId) {
        byte[] keyBytes = Decoders.BASE64.decode(JwtService.DEFAULT_SECRET);
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .claims(Map.of("ROLES", List.of("USER")))
                .subject(userId.toString())
                .issuedAt(new Date(now - 60_000))
                .expiration(new Date(now - 30_000))
                .signWith(Keys.hmacShaKeyFor(keyBytes))
                .compact();
    }
}
