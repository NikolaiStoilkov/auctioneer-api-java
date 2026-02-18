package com.auctioneer.transformers;

import com.auctioneer.domain.entities.User;
import com.auctioneer.dtos.UserDto;
import com.auctioneer.dtos.forms.UserAuthSignUpDto;
import jakarta.validation.constraints.Null;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;


@Component
@RequiredArgsConstructor
public class UserTransformer {

    public User transform (UserDto<User> userDto){
        User user = new User();

        user.setUsername(userDto.getUsername());
        user.setPasswordHash(userDto.getPasswordHash());
        user.setEmail(userDto.getEmail());
        user.setRole(userDto.getRole());
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
            transformedUser.setRole(role);
        }

        return transformedUser;
    }

    public User transform (UserAuthSignUpDto userAuthSignUpDto) {
        User user = new User();

        user.setUsername(userAuthSignUpDto.getUsername());
        user.setPasswordHash(userAuthSignUpDto.getPassword());
        user.setRole(userAuthSignUpDto.getRole());
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
