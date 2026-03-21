package com.auctioneer.transformers.user;

import com.auctioneer.domain.entities.User;
import com.auctioneer.dtos.user.UserDto;
import com.auctioneer.dtos.forms.UserAuthSignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


@Component
@RequiredArgsConstructor
public class UserTransformer {

    public User transform (UserDto userDto){
        User user = new User();

        user.setUsername(userDto.getUsername());
        user.setPasswordHash(userDto.getPasswordHash());
        user.setEmail(userDto.getEmail());
        user.setRoles(userDto.getRoles());
        user.setFirstName(userDto.getFirstName());
        user.setMiddleName(userDto.getMiddleName());
        user.setLastName(userDto.getLastName());
        user.setUcn(userDto.getUcn());
        user.setCountry(userDto.getCountry());
        user.setCity(userDto.getCity());
        user.setStreet(userDto.getStreet());
        user.setStreetNumber(userDto.getStreetNumber());
        user.setPostalCode(userDto.getPostalCode());
        user.setPhoneNumber(userDto.getPhoneNumber());

        return user;
    }

    public User transform(UserDetails user) {
        User transformedUser = new User();
        transformedUser.setUsername(user.getUsername());
        transformedUser.setPasswordHash(user.getPassword());

        // Assuming the role is stored as a GrantedAuthority
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        if (!authorities.isEmpty()) {
            String role = authorities.iterator().next().getAuthority();

            assert role != null;
            transformedUser.setRoles(List.of(role));
        }

        return transformedUser;
    }

    public User transform (UserAuthSignUpDto userAuthSignUpDto) {
        User user = new User();

        user.setUsername(userAuthSignUpDto.getUsername());
        user.setPasswordHash(userAuthSignUpDto.getPassword());
        user.setRoles(userAuthSignUpDto.getRoles());
        user.setFirstName(userAuthSignUpDto.getFirstName());
        user.setMiddleName(userAuthSignUpDto.getMiddleName());
        user.setLastName(userAuthSignUpDto.getLastName());
        user.setUcn(userAuthSignUpDto.getUcn());
        user.setCountry(userAuthSignUpDto.getCountry());
        user.setCity(userAuthSignUpDto.getCity());
        user.setStreet(userAuthSignUpDto.getStreet());
        user.setStreetNumber(userAuthSignUpDto.getStreetNumber());
        user.setPostalCode(userAuthSignUpDto.getPostalCode());
        user.setPhoneNumber(userAuthSignUpDto.getPhoneNumber());
        user.setEmail(userAuthSignUpDto.getEmail());

        return user;
    }

}
