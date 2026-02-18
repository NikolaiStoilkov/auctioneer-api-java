package com.auctioneer.transformers;

import com.auctioneer.domain.entities.User;
import com.auctioneer.dtos.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDtoTransformer {
    public UserDto<User> transform(User user) {
        UserDto<User> userDto = new UserDto<>();

        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setPasswordHash(user.getPasswordHash());
        userDto.setRole(user.getRole());
        userDto.setFirstName(user.getFirstName());
        userDto.setMiddleName(user.getMiddleName());
        userDto.setLastName(user.getLastName());
        userDto.setUcn(user.getUcn());
        userDto.setCountry(user.getCountry());
        userDto.setCity(user.getCity());
        userDto.setStreet(user.getStreet());
        userDto.setStreetNumber(user.getStreetNumber());
        userDto.setPostalCode(user.getPostalCode());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setEmail(user.getEmail());

        return userDto;
    }

    public UserDto<User> transform (UserDetails userDetails){
        UserDto<User> userDto = new UserDto<>();

        userDto.setUsername(userDetails.getUsername());
        userDto.setPasswordHash(userDetails.getPassword());

        return userDto;
    }
}
