package com.auctioneer.integration;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthenticationIntegrationTest extends IntegrationTestBase {

    @Test
    void signUpReturnsValidJwtAndPersistsUser() throws Exception {
        String token = signUp("signup1");

        assertThat(token).isNotBlank();
        // A JWT has three dot-separated parts and must carry the new user's id
        assertThat(token.chars().filter(c -> c == '.').count()).isEqualTo(2);
        Long userId = userIdFromToken(token);
        assertThat(userRepository.findById(userId)).isPresent();
        assertThat(userRepository.findById(userId).get().getUsername()).isEqualTo("usersignup1");
    }

    @Test
    void signUpStoresHashedPasswordNotPlaintext() throws Exception {
        String token = signUp("signup2");

        String hash = userRepository.findById(userIdFromToken(token)).get().getPasswordHash();
        assertThat(hash).isNotEqualTo("password123").startsWith("$2"); // bcrypt prefix
    }

    @Test
    void signUpDuplicateUsernameReturnsConflict() throws Exception {
        signUp("dupuser");

        Map<String, Object> second = signUpPayload("dupuser");
        second.put("email", "other@example.com");

        mockMvc.perform(post("/api/auth/sign-up")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(second)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Username already exists"));
    }

    @Test
    void signUpDuplicateEmailReturnsConflict() throws Exception {
        signUp("dupemail");

        Map<String, Object> second = signUpPayload("otheruser");
        second.put("email", "userdupemail@example.com");

        mockMvc.perform(post("/api/auth/sign-up")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(second)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Email already exists"));
    }

    @Test
    void signUpInvalidPayloadReturnsValidationError() throws Exception {
        Map<String, Object> body = signUpPayload("badpayload");
        body.put("password", "123");        // too short
        body.remove("firstName");           // missing

        mockMvc.perform(post("/api/auth/sign-up")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void signInWithCorrectCredentialsReturnsJwt() throws Exception {
        signUp("signin1");

        String token = mockMvc.perform(post("/api/auth/sign-in")
                        .contentType(APPLICATION_JSON)
                        .content("{\"username\": \"usersignin1\", \"password\": \"password123\"}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(token).isNotBlank();
        assertThat(jwtService.isTokenExpired(token)).isFalse();
    }

    @Test
    void signInWithWrongPasswordIsRejected() throws Exception {
        signUp("signin2");

        mockMvc.perform(post("/api/auth/sign-in")
                        .contentType(APPLICATION_JSON)
                        .content("{\"username\": \"usersignin2\", \"password\": \"wrong-password\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid credentials"));
    }

    @Test
    void signInUnknownUserReturnsNotFound() throws Exception {
        mockMvc.perform(post("/api/auth/sign-in")
                        .contentType(APPLICATION_JSON)
                        .content("{\"username\": \"ghost\", \"password\": \"password123\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User not found: ghost"));
    }

    @Test
    void signInMissingFieldsReturnsValidationError() throws Exception {
        mockMvc.perform(post("/api/auth/sign-in")
                        .contentType(APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
