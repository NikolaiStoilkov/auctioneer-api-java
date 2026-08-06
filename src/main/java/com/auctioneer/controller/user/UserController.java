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

    /**
     * Returns a user by id.
     *
     * @param id        the id of the user
     * @param principal the authenticated user
     * @return the user
     */
    @GetMapping("/{id}")
    public UserDto get(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal principal) {
        return userService.get(id);
    }

    /**
     * Creates a new user.
     *
     * @param userDto   the user to create
     * @param principal the authenticated user
     */
    @PostMapping("/save")
    public void create(@Valid @RequestBody UserDto userDto, @AuthenticationPrincipal UserPrincipal principal) {
        userService.create(userDto);
    }

    /**
     * Updates the authenticated user's profile.
     *
     * @param userDto   the new user data
     * @param principal the authenticated user
     */
    @PatchMapping("/edit")
    public void edit(@Valid @RequestBody UserDto userDto, @AuthenticationPrincipal UserPrincipal principal) {
        userService.edit(principal.getId(), userDto);
    }

    /**
     * Deletes a user by id.
     *
     * @param id        the id of the user to delete
     * @param principal the authenticated user
     */
    @DeleteMapping("/request/delete/{id}")
    public void delete(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal principal) {
        userService.delete(id);
    }
}
