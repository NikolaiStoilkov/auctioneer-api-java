package com.auctioneer.config;

import com.auctioneer.filters.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
                .csrf(AbstractHttpConfigurer::disable)
                // .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/api/auth/sign-in", "/api/auth/sign-up").permitAll()

                        // Ad endpoints
                        .requestMatchers("/api/ads/{id}").permitAll()
                        .requestMatchers("/api/ads/pagination").permitAll()
                        .requestMatchers("/api/ads/my-ads").hasAuthority("USER")
                        .requestMatchers("/api/ads/create").hasAuthority("USER")
                        .requestMatchers("/api/ads/edit/{id}").hasAuthority("USER")
                        .requestMatchers("/api/ads/bid/{id}").hasAuthority("USER")

                        // User endpoints
                        .requestMatchers("/api/users/{id}").hasAuthority("USER")
                        .requestMatchers("/api/users/save").hasAuthority("USER")
                        .requestMatchers("/api/users/edit").hasAuthority("USER")
                        .requestMatchers("/api/users/request/delete/{id}").hasAuthority("USER")

                        // Shipping endpoints
                        .requestMatchers("/api/shipping/{userId}").hasAuthority("USER")
                        .requestMatchers("/api/shipping/save").hasAuthority("USER")
                        .requestMatchers("/api/shipping/edit/{id}").hasAuthority("USER")

                        // Comment endpoints
                        .requestMatchers("/api/comments/{adId}").permitAll()
                        .requestMatchers("/api/comments/create/{adId}").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers("/api/comments/edit/{adId}").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers("/api/comments/delete/{id}").hasAnyAuthority("USER", "ADMIN")

                        // All other endpoints require authentication
                        .anyRequest().authenticated()
                )

                // Stateless session (required for JWT)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Add JWT filter before Spring Security's default filter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
