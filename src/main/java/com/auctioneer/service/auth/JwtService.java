package com.auctioneer.service.auth;

import com.auctioneer.exceptions.InvalidCredentialsException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

import java.util.Date;
import java.util.Map;
import java.security.Key;
import java.util.function.Function;

@Component
public class JwtService {

    /**
     * Development fallback signing key, used only when no {@code jwt.secret}
     * (env var {@code JWT_SECRET}) is configured — e.g. in unit tests that
     * instantiate this class directly. Production must supply {@code JWT_SECRET}.
     */
    public static final String DEFAULT_SECRET = "5367566859703373367639792F423F452848284D6251655468576D5A71347437";

    @Value("${jwt.secret:}")
    private String configuredSecret;

    private String resolveSecret() {
        return (configuredSecret != null && !configuredSecret.isBlank())
                ? configuredSecret
                : DEFAULT_SECRET;
    }

    public String generateToken(Long userId, Map<String, Object> claims) {
        return Jwts.builder()
                .claims(claims)
                .subject(userId.toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSignKey())
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(resolveSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (ExpiredJwtException ex) {
            // A structurally valid but expired token parses to this exception;
            // treat it as plainly expired rather than propagating.
            return true;
        }
    }

    public String authorize(
            Long userId,
            Boolean isPasswordMatched,
            Map<String, Object> claims
    ) {
        if (isPasswordMatched) {
            return generateToken(userId, claims);
        } else {
            throw new InvalidCredentialsException();
        }
    }

    public Boolean verify(String token) {
        try {
            return extractAllClaims(token).getExpiration().before(new Date());
        } catch (Exception ex) {
            return false;
        }
    }
}

