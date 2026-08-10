package com.auctioneer.transformers.user;

import com.auctioneer.domain.entities.User;
import com.auctioneer.dtos.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Builds {@link UserDto} instances from a {@link User} entity or a Spring
 * Security {@link UserDetails}.
 */
@Component
@RequiredArgsConstructor
public class UserDtoTransformer {
    /**
     * Maps a {@link User} entity to a {@link UserDto}.
     *
     * @param user the source entity
     * @return the mapped DTO
     */
    public UserDto transform(User user) {
        UserDto userDto = new UserDto();

        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setPasswordHash(user.getPasswordHash());
        userDto.setRoles(user.getRoles());
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

    /**
     * Maps a Spring Security {@link UserDetails} to a {@link UserDto},
     * copying the username and password only.
     *
     * @param userDetails the security principal
     * @return the mapped DTO
     */
    public UserDto transform (UserDetails userDetails){
        UserDto userDto = new UserDto();

        userDto.setUsername(userDetails.getUsername());
        userDto.setPasswordHash(userDetails.getPassword());

        return userDto;
    }
}
