package com.auctioneer.controller;

import com.auctioneer.dtos.UserDto;
import com.auctioneer.domain.entities.User;
import com.auctioneer.repository.UserRepository;

import com.auctioneer.transformers.UserTransformer;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final UserTransformer userTransformer;

    @GetMapping("/{id}")
    public UserDto<User> get(@PathVariable Long id) {
        UserDto<User> userData = new UserDto<User>();

        userRepository.findById(id).ifPresent(user -> {
            userData.setUsername(user.getUsername());
            userData.setPasswordHash(user.getPasswordHash());
            userData.setEmail(user.getEmail());
        });

        return userData;
    }

    @PostMapping("/save")
    public void save(@RequestBody UserDto<User> userDto) {
        //TODO: add validation for the user data

        User user = userTransformer.transform(userDto);

        userRepository.save(user);
    }
}
