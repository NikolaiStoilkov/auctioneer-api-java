package com.auctioneer.service;

import com.auctioneer.dtos.AuthDto;
import com.auctioneer.domain.entities.User;
import com.auctioneer.dtos.forms.UserAuthSignUpDto;
import com.auctioneer.repository.UserRepository;
import com.auctioneer.transformers.UserDetailsTransformer;

import com.auctioneer.transformers.UserTransformer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final UserDetailsTransformer userDetailsTransformer;
    private final UserTransformer userTransformer;

    public AuthDto create(UserAuthSignUpDto userAuthSignUpDto) {
        String username = userAuthSignUpDto.getUsername();
        String password = userAuthSignUpDto.getPassword(); // plain text password

        String passwordHash = passwordEncoder.encode(password);
        userAuthSignUpDto.setPassword(passwordHash);

        Map<String, Object> claims = Map.of("ROLES", List.of("USER"));

        User user = userTransformer.transform(userAuthSignUpDto);
//        UserDetails userDetails = userDetailsTransformer.transform(user);
        userRepository.save(user);

        String token = jwtService.generateToken(
                user.getId(),
                claims
        );
//
        AuthDto authDto = new AuthDto();
//
//        authDto.setUserDetails(
//                userDetails
//        );
//
        authDto.setUser(user);

        authDto.setToken(token);

        return authDto;
    }

    public AuthDto verify(String token, String username) {
        System.out.println("Verifying token for user: " + username);

        User user = userRepository.findUserByUsername(username);

        UserDetails userDetails = userDetailsTransformer.transform(user);

        try {
            if (jwtService.validateToken(token, userDetails)) {
                AuthDto authDto = new AuthDto();
                authDto.setUser(user);
//                authDto.setUserDetails(userDetails);
                authDto.setToken(token);

                return authDto;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
