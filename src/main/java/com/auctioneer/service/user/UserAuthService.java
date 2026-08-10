package com.auctioneer.service.user;

import com.auctioneer.domain.entities.User;
import com.auctioneer.dtos.forms.UserAuthSignInDto;
import com.auctioneer.dtos.forms.UserAuthSignUpDto;
import com.auctioneer.exceptions.EmailAlreadyExistsException;
import com.auctioneer.exceptions.UserNotFoundException;
import com.auctioneer.exceptions.UsernameAlreadyExistsException;
import com.auctioneer.repository.user.UserRepository;
import com.auctioneer.service.auth.AuthenticationService;
import com.auctioneer.service.discordNotifications.DiscordService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * Handles user registration and login, issuing a JWT on success.
 */
@Service
@RequiredArgsConstructor
public class UserAuthService {
    private final AuthenticationService authenticationService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final DiscordService discordService;

    /**
     * Registers a new user and returns a freshly issued JWT.
     *
     * @param userAuthSignUpDto the sign-up form
     * @return the authentication token
     * @throws UsernameAlreadyExistsException if the username is taken
     * @throws EmailAlreadyExistsException    if the email is taken
     */
    public String signUp(UserAuthSignUpDto userAuthSignUpDto) {
        String password = userAuthSignUpDto.getPassword();
        String passwordHash = passwordEncoder.encode(password);

        if (userRepository.existsUserByUsername(userAuthSignUpDto.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }

        if (userRepository.existsUserByEmail(userAuthSignUpDto.getEmail())) {
            throw new EmailAlreadyExistsException();
        }

        Map<String, Object> claims = Map.of("ROLES", List.of("USER"));

        User user = new User();

        BeanUtils.copyProperties(userAuthSignUpDto, user);
        user.setPasswordHash(passwordHash);

        Long userId = userRepository.save(user).getId();

        discordService.sendUserNotification("👤 New user registered: **" + userAuthSignUpDto.getUsername() + "**");

        return authenticationService.initialize(userId, claims);
    }

    /**
     * Authenticates a user by username and password and returns a JWT.
     *
     * @param userAuthSignInDto the sign-in form
     * @return the authentication token
     * @throws UserNotFoundException if no user has the given username
     */
    public String signIn(UserAuthSignInDto userAuthSignInDto) {
        String username = userAuthSignInDto.getUsername();
        String password = userAuthSignInDto.getPassword();

        User user = userRepository.findUserByUsername(username);

        if (user == null) {
            throw new UserNotFoundException(username);
        }

        Long userId = user.getId();
        String storedPassword = user.getPasswordHash();

        Boolean isPasswordMatched = passwordEncoder.matches(password, storedPassword);

        Map<String, Object> claims = Map.of("ROLES", user.getRoles());

        discordService.sendUserNotification("🔑 User signed in: **" + username + "**");

        return authenticationService.authorize(userId, isPasswordMatched, claims);
    }
}
