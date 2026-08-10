package com.auctioneer.transformers.user;

import com.auctioneer.domain.entities.User;
import com.auctioneer.dtos.user.UserDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Adapts a {@link User} entity or {@link UserDto} into a Spring Security
 * {@link UserDetails} for authentication. Account flags are always active.
 */
@Component
@RequiredArgsConstructor
public class UserDetailsTransformer {

    /**
     * Adapts a {@link User} entity into a {@link UserDetails}.
     *
     * @param user the source entity
     * @return the security principal
     */
    public UserDetails transform(User user) {
        return new UserDetails() {
            @Override
            @NonNull
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return user.getRoles().stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();
            }

            @Override
            public String getPassword() {
                return user.getPasswordHash();
            }

            @Override
            @NonNull
            public String getUsername() {
                return user.getUsername();
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };
    }

    /**
     * Adapts a {@link UserDto} into a {@link UserDetails}.
     *
     * @param userDto the source DTO
     * @return the security principal
     */
    public UserDetails  transform (UserDto userDto) {
        return new UserDetails() {
            @Override
            @NonNull
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return userDto.getRoles().stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();
            }

            @Override
            public String getPassword() {
                return userDto.getPasswordHash();
            }

            @Override
            @NonNull
            public String getUsername() {
                return userDto.getUsername();
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };
    }
}
