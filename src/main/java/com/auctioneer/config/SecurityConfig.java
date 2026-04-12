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
                        .requestMatchers("/api/ads/my-ads").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers("/api/ads/create").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers("/api/ads/edit/{id}").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers("/api/ads/bid/{id}").hasAnyAuthority("USER", "ADMIN")

                        // User endpoints
                        .requestMatchers("/api/users/{id}").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers("/api/users/save").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers("/api/users/edit").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers("/api/users/request/delete/{id}").hasAnyAuthority("USER")

                        // Shipping endpoints
                        .requestMatchers("/api/shipping/{userId}").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers("/api/shipping/save").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers("/api/shipping/edit/{id}").hasAnyAuthority("USER", "ADMIN")

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
