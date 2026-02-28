package com.auctioneer.service.user;

import com.auctioneer.domain.entities.User;
import com.auctioneer.dtos.user.UserDto;
import com.auctioneer.repository.UserRepository;
import com.auctioneer.transformers.user.UserTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserTransformer userTransformer;

    public UserDto get(Long userId) {
        return null;
    }

    public void create(UserDto userDto) {
        User user = userTransformer.transform(userDto);

        userRepository.save(user);
    }
}
