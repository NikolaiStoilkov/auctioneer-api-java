package com.example.auctioneer.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;

import java.util.Date;
import java.util.Map;
import java.security.Key;
import java.util.HashMap;
import java.util.function.Function;

@Component
public class JwtService {

    public static final String SECRET = "5367566859703373367639792F423F452848284D6251655468576D5A71347437";

    public String generateToken(String username, String passwordHash) { // Use email as username
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username, passwordHash);
    }

    private String createToken(Map<String, Object> claims, String username, String passwordHash) {
        String tokenSubject = username + ":" + passwordHash;

        return Jwts.builder()
                .claims(claims)
                .subject(tokenSubject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSignKey())
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractPasswordHash(String token) {
        return extractClaim(token, claims -> claims.get("passwordHash", String.class));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String tokenUsername = extractUsername(token);
        final String tokenPasswordHash = extractPasswordHash(token);


        return (
                tokenUsername.equals(userDetails.getUsername())
                        && !isTokenExpired(token)
        );
    }

    // This method is used in jwtAuthorization filter to check if the password hash in the token matches the current password hash of the user
    public Boolean validateToken(String token, UserDetails userDetails, UserDetailsPasswordService userDetailsPasswordService) {
        final String tokenSubject = extractUsername(token);
        final String[] parts = tokenSubject.split(":");
        final String tokenUsername = parts[0];
        final String tokenPasswordHash = parts.length > 1 ? parts[1] : "";

        return (
                tokenUsername.equals(userDetails.getUsername())
                        && !isTokenExpired(token)
                        && tokenPasswordHash.equals(userDetails.getPassword())
        );
    }
}

