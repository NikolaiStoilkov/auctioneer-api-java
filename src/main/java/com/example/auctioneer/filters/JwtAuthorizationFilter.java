package com.example.auctioneer.filters;

import com.example.auctioneer.dtos.JwtDto;
import com.example.auctioneer.service.JwtService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class JwtAuthorizationFilter {
    private final JwtService jwtService;
    private final UserDetails userDetails;
    private final UserDetailsPasswordService userDetailsPasswordService;

    public JwtAuthorizationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
        this.userDetails = null;
        this.userDetailsPasswordService = null;
    }

    public void GetUsernameAndPassword(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        JwtDto jwtDto = new JwtDto();

        assert userDetails != null; // Ensure userDetails is not null before proceeding
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities());

            jwtDto.setToken(authHeader.substring(7));

            // Extract username and passwordHash from token
            jwtDto.setUsername(jwtService.extractUsername(jwtDto.getToken()));
            jwtDto.setPasswordHash(jwtService.extractPasswordHash(jwtDto.getToken()));
        }
    }

    public String createAuthenticationToken(UserDetails userDetails, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        JwtDto jwtDto = new JwtDto();

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities());

            jwtDto.setToken(authHeader.substring(7));

            // Extract username from token
            jwtDto.setUsername(jwtService.extractUsername(jwtDto.getToken()));
            jwtDto.setPasswordHash(jwtService.extractPasswordHash(jwtDto.getToken()));

            // Set authentication in security context
            String generatedToken = jwtService.generateToken(jwtDto.getUsername(), jwtDto.getPasswordHash());

            return generatedToken;
        }

        return null;
    }
}