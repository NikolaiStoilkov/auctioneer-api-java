package com.auctioneer.config;

import com.auctioneer.filters.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/api/auth/sign-in", "/api/auth/sign-up").permitAll()
                        .requestMatchers("/error").permitAll()

//                        // WebSocket handshake
//                        .requestMatchers("/ws/**").permitAll()

                        // Ad endpoints
                        .requestMatchers("/api/ads/{id}").permitAll()
                        .requestMatchers("/api/ads/{id}/stream").permitAll()
                        .requestMatchers("/api/ads/stream").permitAll()
                        .requestMatchers("/api/ads/pagination").permitAll()
                        .requestMatchers("/api/ads/my-ads").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers("/api/ads/create").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers("/api/ads/edit/{id}").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers("/api/ads/bid/{id}").hasAnyAuthority("USER", "ADMIN")

                        // User endpoints
                        .requestMatchers("/api/users/notifications/stream").permitAll()
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

                        // Stripe
                        .requestMatchers(HttpMethod.GET, "/api/stripe/config").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/stripe/auth").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/stripe/webhook").permitAll()

                        // Wallet endpoints
                        .requestMatchers("/api/wallet/**").hasAnyAuthority("USER", "ADMIN")

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
