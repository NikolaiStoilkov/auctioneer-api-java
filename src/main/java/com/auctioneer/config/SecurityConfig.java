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

import java.util.List;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * CORS policy for the API: any origin pattern, standard verbs, credentials
     * allowed.
     *
     * @return the CORS configuration source
     */
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

    /**
     * Stateless (JWT) security filter chain: declares public vs role-guarded
     * routes (method + path) and installs the JWT authentication filter.
     *
     * @param http the security builder
     * @return the built filter chain
     */
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

                        // Image endpoints — download is public, upload requires auth
                        .requestMatchers(HttpMethod.GET, "/api/images/{code}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/images").hasAnyAuthority("USER", "ADMIN")

                        // Ad endpoints — specific paths must come before the "/api/ads/{id}"
                        // wildcard, otherwise {id} matches "my-ads" and makes it public
                        .requestMatchers("/api/ads/my-ads").hasAnyAuthority("USER", "ADMIN")
                        // Pub/sub push endpoint (Cloud Scheduler cannot send a user JWT)
                        .requestMatchers(HttpMethod.POST, "/api/ads/close-expired").permitAll()
                        // Noun-based ad routes: verb carried by the HTTP method
                        .requestMatchers(HttpMethod.POST, "/api/ads").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/ads/{id}").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/ads/{id}/bids").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/ads/{id}").permitAll()
                        .requestMatchers("/api/ads/{id}/stream").permitAll()
                        .requestMatchers("/api/ads/stream").permitAll()
                        .requestMatchers("/api/ads/pagination").permitAll()

                        // User endpoints
                        .requestMatchers("/api/users/notifications/stream").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/{id}").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/users").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/users").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasAnyAuthority("USER")

                        // Shipping endpoints
                        .requestMatchers(HttpMethod.GET, "/api/shipping/{userId}").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/shipping").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/shipping/{id}").hasAnyAuthority("USER", "ADMIN")

                        // Comment endpoints
                        .requestMatchers(HttpMethod.GET, "/api/comments/{adId}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/comments").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/comments").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/comments/{id}").hasAnyAuthority("USER", "ADMIN")

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

    /**
     * BCrypt password encoder used for hashing and verifying credentials.
     *
     * @return the password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
