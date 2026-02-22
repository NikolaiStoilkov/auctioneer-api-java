package com.auctioneer.service;

import com.auctioneer.dtos.forms.UserAuthSignInDto;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.config.annotation.web.oauth2.resourceserver.JwtDsl;
import org.springframework.stereotype.Component;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;

import java.util.Date;
import java.util.Map;
import java.security.Key;
import java.util.function.Function;

@Component
public class JwtService {

    public static final String SECRET = "5367566859703373367639792F423F452848284D6251655468576D5A71347437";

    public String generateToken(Long userId, Map<String, Object> claims) { // Use email as username
        return Jwts.builder()
                .claims(claims)
                .subject(userId.toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSignKey())
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
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
        return extractExpiration(token).before(new Date());
    }

    public UserAuthSignInDto parseToken(String token) {
        String tokenSubject = extractUserId(token);
        String[] parts = tokenSubject.split(":");
        String username = parts[0];
        String passwordHash = parts.length > 1 ? parts[1] : "";

        UserAuthSignInDto userAuthDto = new UserAuthSignInDto();
        userAuthDto.setUsername(username);
        userAuthDto.setPasswordHash(passwordHash);

        return userAuthDto;
    }

    public Boolean validateToken(String token) {
        try {
            return extractAllClaims(token).getExpiration().before(new Date());
        } catch (Exception ex) {
            return false;
        }
    }
}

