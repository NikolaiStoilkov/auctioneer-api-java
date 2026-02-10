package com.example.auctioneer.controller;

import com.example.auctioneer.dtos.UserDto;
import com.example.auctioneer.domain.entities.User;
import com.example.auctioneer.repository.UserRepository;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public User get(@PathVariable Long id) {
        User userData = new User(); // Should I use userDto instead of user?

        userRepository.findById(id).ifPresent(user -> {
            userData.setUsername(user.getUsername());
            userData.setPasswordHash(user.getPasswordHash());
            userData.setEmail(user.getEmail());
        });

        return userData;
    }

    @PostMapping("/save")
    public void save(@RequestBody UserDto<User> user) {
        System.out.println("Received user data: " +
                "Username: " + user.getUsername() + ", " +
                "Password Hash: " + user.getPasswordHash() + ", " +
                "Role: " + user.getRole() + ", " +
                "First Name: " + user.getFirstName() + ", " +
                "Middle Name: " + user.getMiddleName() + ", " +
                "Last Name: " + user.getLastName() + ", " +
                "UCN: " + user.getUcn() + ", " +
                "Country: " + user.getCountry() + ", " +
                "City: " + user.getCity() + ", " +
                "Street: " + user.getStreet() + ", " +
                "Street Number: " + user.getStreetNumber() + ", " +
                "Postal Code: " + user.getPostalCode() + ", " +
                "Phone Number: " + user.getPhoneNumber() + ", " +
                "Email: " + user.getEmail() + ", "
                ); // Debugging line
        User userData = new User(); // Should I use userDto instead of user?

        userData.setUsername(user.getUsername());
        userData.setPasswordHash(user.getPasswordHash());
        userData.setRole(user.getRole());
        userData.setFirstName(user.getFirstName());
        userData.setMiddleName(user.getMiddleName());
        userData.setLastName(user.getLastName());
        userData.setUcn(user.getUcn());
        userData.setCountry(user.getCountry());
        userData.setCity(user.getCity());
        userData.setStreet(user.getStreet());
        userData.setStreetNumber(user.getStreetNumber());
        userData.setPostalCode(user.getPostalCode());
        userData.setPhoneNumber(user.getPhoneNumber());
        userData.setEmail(user.getEmail());

        userRepository.save(userData);
    }
}
