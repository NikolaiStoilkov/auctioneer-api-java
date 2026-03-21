package com.auctioneer.controller.user;

import com.auctioneer.dtos.user.UserDto;

import com.auctioneer.dtos.user.UserPrincipal;
import com.auctioneer.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public UserDto get(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal principal) {
        return userService.get(id);
    }

    @PostMapping("/save")
    public void create(@Valid @RequestBody UserDto userDto, @AuthenticationPrincipal UserPrincipal principal) {
        userService.create(userDto);
    }

    @PatchMapping("/edit")
    public void edit(@Valid @RequestBody UserDto userDto, @AuthenticationPrincipal UserPrincipal principal) {
        userService.edit(userDto);
    }

    @DeleteMapping("/request/delete/{id}")
    public void delete(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal principal) {
        userService.delete(id);
    }
}
