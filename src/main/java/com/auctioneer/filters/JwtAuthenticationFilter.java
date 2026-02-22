package com.auctioneer.filters;

import com.auctioneer.dtos.UserPrincipal;
import com.auctioneer.repository.UserRepository;
import com.auctioneer.service.JwtService;

import com.auctioneer.transformers.UserDetailsTransformer;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserDetailsTransformer userDetailsTransformer;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String userId = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            userId = jwtService.extractUserId(token);
        } else {
            response.setStatus(403);
            return;
        }

        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null
                && !jwtService.isTokenExpired(token)) {

            UserPrincipal principal = UserPrincipal.builder()
                    .id(Long.valueOf(userId))
                    .build();

            Claims claims = jwtService.extractAllClaims(token);
            List<SimpleGrantedAuthority> roles = claims.get("ROLES", List.class)
                    .stream()
                    .map(r -> new SimpleGrantedAuthority(r.toString()))
                    .toList();

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    principal,
                    null,
                    roles);

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authToken);
        } else {
            response.setStatus(403);
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/auth/") || path.startsWith("/api/users/");
    }
}
