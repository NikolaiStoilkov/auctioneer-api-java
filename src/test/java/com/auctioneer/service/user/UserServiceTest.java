package com.auctioneer.service.user;

import com.auctioneer.domain.entities.User;
import com.auctioneer.dtos.user.UserDto;
import com.auctioneer.repository.user.UserRepository;
import com.auctioneer.service.discordNotifications.DiscordService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import com.auctioneer.exceptions.UserNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DiscordService discordService;

    @InjectMocks
    private UserService userService;

    private User sampleUser;
    private UserDto sampleUserDto;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder()
                .id(1L)
                .username("john_doe")
                .passwordHash("hashedpassword")
                .firstName("John")
                .middleName("Michael")
                .lastName("Doe")
                .ucn("1234567890")
                .country("Bulgaria")
                .city("Sofia")
                .street("Vitosha Blvd")
                .streetNumber("101")
                .postalCode("1000")
                .phoneNumber("+359888111222")
                .email("john@example.com")
                .roles(List.of("USER"))
                .build();

        sampleUserDto = new UserDto();
        sampleUserDto.setUsername("john_doe");
        sampleUserDto.setPasswordHash("hashedpassword");
        sampleUserDto.setFirstName("John");
        sampleUserDto.setMiddleName("Michael");
        sampleUserDto.setLastName("Doe");
        sampleUserDto.setUcn("1234567890");
        sampleUserDto.setCountry("Bulgaria");
        sampleUserDto.setCity("Sofia");
        sampleUserDto.setStreet("Vitosha Blvd");
        sampleUserDto.setStreetNumber("101");
        sampleUserDto.setPostalCode("1000");
        sampleUserDto.setPhoneNumber("+359888111222");
        sampleUserDto.setEmail("john@example.com");
        sampleUserDto.setRoles(List.of("USER"));
    }

    @Test
    void getShouldReturnUserDtoWhenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        UserDto result = userService.get(1L);

        assertNotNull(result);
        assertEquals("john_doe", result.getUsername());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("john@example.com", result.getEmail());
        assertEquals("1234567890", result.getUcn());
        verify(userRepository).findById(1L);
    }

    @Test
    void getShouldThrowExceptionWhenUserDoesNotExist() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.get(99L));
        verify(userRepository).findById(99L);
    }

    @Test
    void createShouldSaveUser() {
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        userService.create(sampleUserDto);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void editShouldSaveEditedUser() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        userService.edit(1L, sampleUserDto);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void deleteShouldDeleteUser() {
        doNothing().when(userRepository).deleteById(1L);

        userService.delete(1L);

        verify(userRepository).deleteById(1L);
    }
}
