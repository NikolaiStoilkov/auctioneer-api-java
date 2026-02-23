package com.auctioneer.service.user;

import com.auctioneer.domain.entities.User;
import com.auctioneer.dtos.forms.UserAuthSignInDto;
import com.auctioneer.dtos.forms.UserAuthSignUpDto;
import com.auctioneer.repository.UserRepository;
import com.auctioneer.service.auth.AuthenticationService;
import com.auctioneer.transformers.user.UserTransformer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class UserAuthService {
    private final AuthenticationService authenticationService;
    private final PasswordEncoder passwordEncoder;
    private final UserTransformer userTransformer;
    private final UserRepository userRepository;

    public String signUp(UserAuthSignUpDto userAuthSignUpDto) {
        String password = userAuthSignUpDto.getPassword(); // plain text password
        String passwordHash = passwordEncoder.encode(password);

        // TODO: Refactor with better error handling and validation
        if (userRepository.existsUserByUsername(userAuthSignUpDto.getUsername())) {
            return "Username already exists";
        }

        if (userRepository.existsUserByEmail(userAuthSignUpDto.getEmail())) {
            return "Email already exists";
        }

        userAuthSignUpDto.setPassword(passwordHash);

        Map<String, Object> claims = Map.of("ROLES", List.of("USER"));

        User transformedUser = userTransformer.transform(userAuthSignUpDto);

        Long userId = userRepository.save(transformedUser)
                .getId();

        return authenticationService.initialize(
                userId,
                claims
        );
    }

    public String signIn(UserAuthSignInDto userAuthSignInDto) {
        String username = userAuthSignInDto.getUsername();
        String password = userAuthSignInDto.getPassword(); // plain text password

        User user = userRepository.findUserByUsername(username);

        // TODO: Refactor with better error handling and validation
        if (user == null) {
            return "User not found";
        }

        Long userId = user.getId();
        String storedPassword = user.getPasswordHash();

        Boolean isPasswordMatched = passwordEncoder.matches(password, storedPassword);

        Map<String, Object> claims = Map.of("ROLES", user.getRole());

        return authenticationService.authorize(
                userId,
                isPasswordMatched,
                claims
        );
    }


}
