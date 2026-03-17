package com.auctioneer.service.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationService authenticationService;

    private Map<String, Object> claims;

    @BeforeEach
    void setUp() {
        claims = Map.of("ROLES", List.of("USER"));
    }

    @Test
    void initialize_shouldReturnToken() {
        when(jwtService.generateToken(1L, claims)).thenReturn("jwt-token");

        String result = authenticationService.initialize(1L, claims);

        assertEquals("jwt-token", result);
        verify(jwtService).generateToken(1L, claims);
    }

    @Test
    void authorize_shouldReturnToken_whenPasswordMatches() {
        when(jwtService.authorize(1L, true, claims)).thenReturn("jwt-token");

        String result = authenticationService.authorize(1L, true, claims);

        assertEquals("jwt-token", result);
        verify(jwtService).authorize(1L, true, claims);
    }

    @Test
    void authorize_shouldThrowException_whenPasswordDoesNotMatch() {
        when(jwtService.authorize(1L, false, claims))
                .thenThrow(new RuntimeException("Invalid credentials"));

        assertThrows(RuntimeException.class,
                () -> authenticationService.authorize(1L, false, claims));
        verify(jwtService).authorize(1L, false, claims);
    }

    @Test
    void verify_shouldReturnTrue_whenTokenIsValid() {
        when(jwtService.verify("valid-token")).thenReturn(true);

        Boolean result = authenticationService.verify("valid-token");

        assertTrue(result);
        verify(jwtService).verify("valid-token");
    }

    @Test
    void verify_shouldReturnFalse_whenTokenIsInvalid() {
        when(jwtService.verify("invalid-token")).thenReturn(false);

        Boolean result = authenticationService.verify("invalid-token");

        assertFalse(result);
        verify(jwtService).verify("invalid-token");
    }
}

