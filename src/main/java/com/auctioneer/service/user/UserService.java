package com.auctioneer.service.user;

import com.auctioneer.domain.entities.User;
import com.auctioneer.dtos.user.UserDto;
import com.auctioneer.exceptions.UserNotFoundException;
import com.auctioneer.repository.user.UserRepository;
import com.auctioneer.service.discordNotifications.DiscordService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * CRUD operations for {@link User} profiles, with Discord notifications on
 * each change.
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final DiscordService discordService;

    /**
     * Returns a user by id.
     *
     * @param userId the id of the user
     * @return the user
     * @throws UserNotFoundException if the user does not exist
     */
    public UserDto get(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        UserDto userDto = new UserDto();

        BeanUtils.copyProperties(user, userDto);

        return userDto;
    }

    /**
     * Creates a new user.
     *
     * @param userDto the user to create
     */
    public void create(UserDto userDto) {
        User user = new User();

        BeanUtils.copyProperties(userDto, user);

        userRepository.save(user);
        discordService.sendUserNotification("👤 User created: **" + userDto.getUsername() + "**");
    }

    /**
     * Updates an existing user's profile (the id is never overwritten).
     *
     * @param userId  the id of the user to update
     * @param userDto the new profile data
     * @throws UserNotFoundException if the user does not exist
     */
    public void edit(Long userId, UserDto userDto) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        BeanUtils.copyProperties(userDto, existingUser, "id");

        userRepository.save(existingUser);
        discordService.sendUserNotification("✏️ User " + existingUser.getFirstName() + " " + existingUser.getLastName()  + " profile updated");
    }

    /**
     * Deletes a user by id.
     *
     * @param userId the id of the user to delete
     */
    public void delete(Long userId) {
        userRepository.deleteById(userId);
        discordService.sendUserNotification("🗑️ User " + userId + " deleted");
    }
}
