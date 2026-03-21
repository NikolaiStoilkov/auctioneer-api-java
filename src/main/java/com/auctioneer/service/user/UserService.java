package com.auctioneer.service.user;

import com.auctioneer.domain.entities.User;
import com.auctioneer.dtos.user.UserDto;
import com.auctioneer.repository.user.UserRepository;
import com.auctioneer.transformers.user.UserTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

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
    }

    public void edit(UserDto userDto) {
        User user = new User();

        BeanUtils.copyProperties(userDto, user);

        userRepository.save(user);
    }

    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }
}
