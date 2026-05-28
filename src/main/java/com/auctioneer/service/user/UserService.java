package com.auctioneer.service.user;

import com.auctioneer.domain.entities.User;
import com.auctioneer.dtos.user.UserDto;
import com.auctioneer.repository.user.UserRepository;
import com.auctioneer.service.discordNotifications.DiscordService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final DiscordService discordService;

    public UserDto get(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        UserDto userDto = new UserDto();

        BeanUtils.copyProperties(user, userDto);

        return userDto;
    }

    public void create(UserDto userDto) {
        User user = new User();

        BeanUtils.copyProperties(userDto, user);

        userRepository.save(user);
        discordService.sendUserNotification("👤 User created: **" + userDto.getUsername() + "**");
    }

    public void edit(Long userId, UserDto userDto) {
        User existingUser = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("User with id " + userId + " not found")
        );

        BeanUtils.copyProperties(userDto, existingUser, "id");

        userRepository.save(existingUser);
        discordService.sendUserNotification("✏️ User " + existingUser.getFirstName() + " " + existingUser.getLastName()  + " profile updated");
    }

    public void delete(Long userId) {
        userRepository.deleteById(userId);
        discordService.sendUserNotification("🗑️ User " + userId + " deleted");
    }
}
