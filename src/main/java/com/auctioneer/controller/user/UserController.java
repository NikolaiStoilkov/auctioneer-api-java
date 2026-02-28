package com.auctioneer.controller.user;

import com.auctioneer.dtos.user.UserDto;

import com.auctioneer.service.user.UserService;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public UserDto get(@PathVariable Long id) {
        return userService.get(id);
    }

    @PostMapping("/save")
    public void create(@RequestBody UserDto userDto) {
      userService.create(userDto);
    }
}
