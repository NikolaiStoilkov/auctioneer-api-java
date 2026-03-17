package com.auctioneer.service.user;

import com.auctioneer.domain.entities.User;
import com.auctioneer.dtos.forms.UserAuthSignInDto;
import com.auctioneer.dtos.forms.UserAuthSignUpDto;
import com.auctioneer.repository.user.UserRepository;
import com.auctioneer.service.auth.AuthenticationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAuthServiceTest {

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserAuthService userAuthService;

    private UserAuthSignUpDto signUpDto;
    private UserAuthSignInDto signInDto;
    private User sampleUser;

    @BeforeEach
    void setUp() {
        signUpDto = new UserAuthSignUpDto();
        signUpDto.setUsername("john_doe");
        signUpDto.setPassword("password123");
        signUpDto.setFirstName("John");
        signUpDto.setMiddleName("Michael");
        signUpDto.setLastName("Doe");
        signUpDto.setUcn("1234567890");
        signUpDto.setCountry("Bulgaria");
        signUpDto.setCity("Sofia");
        signUpDto.setStreet("Vitosha Blvd");
        signUpDto.setStreetNumber("101");
        signUpDto.setPostalCode("1000");
        signUpDto.setPhoneNumber("+359888111222");
        signUpDto.setEmail("john@example.com");

        signInDto = new UserAuthSignInDto();
        signInDto.setUsername("john_doe");
        signInDto.setPassword("password123");

        sampleUser = User.builder()
                .id(1L)
                .username("john_doe")
                .passwordHash("encodedPassword")
                .firstName("John")
                .lastName("Doe")
                .ucn("1234567890")
                .email("john@example.com")
                .roles(List.of("USER"))
                .build();
    }

    // --- signUp tests ---

    @Test
    void signUpShouldReturnTokenWhenUsernameAndEmailAreUnique() {
        when(userRepository.existsUserByUsername("john_doe")).thenReturn(false);
        when(userRepository.existsUserByEmail("john@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);
        when(authenticationService.initialize(eq(1L), anyMap())).thenReturn("jwt-token");

        String result = userAuthService.signUp(signUpDto);

        assertEquals("jwt-token", result);
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
        verify(authenticationService).initialize(eq(1L), anyMap());
    }

    @Test
    void signUpShouldReturnMessageWhenUsernameAlreadyExists() {
        when(userRepository.existsUserByUsername("john_doe")).thenReturn(true);

        String result = userAuthService.signUp(signUpDto);

        assertEquals("Username already exists", result);
        verify(userRepository, never()).save(any(User.class));
        verify(authenticationService, never()).initialize(anyLong(), anyMap());
    }

    @Test
    void signUpShouldReturnMessageWhenEmailAlreadyExists() {
        when(userRepository.existsUserByUsername("john_doe")).thenReturn(false);
        when(userRepository.existsUserByEmail("john@example.com")).thenReturn(true);

        String result = userAuthService.signUp(signUpDto);

        assertEquals("Email already exists", result);
        verify(userRepository, never()).save(any(User.class));
        verify(authenticationService, never()).initialize(anyLong(), anyMap());
    }

    @Test
    void signUpShouldEncodePasswordBeforeSaving() {
        when(userRepository.existsUserByUsername("john_doe")).thenReturn(false);
        when(userRepository.existsUserByEmail("john@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);
        when(authenticationService.initialize(eq(1L), anyMap())).thenReturn("jwt-token");

        userAuthService.signUp(signUpDto);

        assertEquals("encodedPassword", signUpDto.getPassword());
    }

    // --- signIn tests ---

    @Test
    void signInShouldReturnTokenWhenCredentialsAreValid() {
        when(userRepository.findUserByUsername("john_doe")).thenReturn(sampleUser);
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(authenticationService.authorize(eq(1L), eq(true), anyMap())).thenReturn("jwt-token");

        String result = userAuthService.signIn(signInDto);

        assertEquals("jwt-token", result);
        verify(userRepository).findUserByUsername("john_doe");
        verify(passwordEncoder).matches("password123", "encodedPassword");
        verify(authenticationService).authorize(eq(1L), eq(true), anyMap());
    }

    @Test
    void signInShouldReturnMessageWhenUserNotFound() {
        when(userRepository.findUserByUsername("john_doe")).thenReturn(null);

        String result = userAuthService.signIn(signInDto);

        assertEquals("User not found", result);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(authenticationService, never()).authorize(anyLong(), anyBoolean(), anyMap());
    }

    @Test
    void signInShouldCallAuthorizeWithFalseWhenPasswordDoesNotMatch() {
        when(userRepository.findUserByUsername("john_doe")).thenReturn(sampleUser);
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(false);
        when(authenticationService.authorize(eq(1L), eq(false), anyMap()))
                .thenThrow(new RuntimeException("Invalid credentials"));

        assertThrows(RuntimeException.class, () -> userAuthService.signIn(signInDto));

        verify(authenticationService).authorize(eq(1L), eq(false), anyMap());
    }
}
