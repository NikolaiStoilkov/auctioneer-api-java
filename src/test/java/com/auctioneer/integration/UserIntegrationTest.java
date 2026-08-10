package com.auctioneer.integration;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserIntegrationTest extends IntegrationTestBase {

    /** Complete valid UserDto payload (edit/save require every validated field). */
    private Map<String, Object> userPayload(String username, String email, String ucn) {
        Map<String, Object> body = new HashMap<>();
        body.put("username", username);
        body.put("passwordHash", "password123");
        body.put("roles", List.of("USER"));
        body.put("firstName", "Peter");
        body.put("middleName", "M");
        body.put("lastName", "Petrov");
        body.put("ucn", ucn);
        body.put("country", "Bulgaria");
        body.put("city", "Sofia");
        body.put("street", "Rakovski");
        body.put("streetNumber", "7");
        body.put("postalCode", "1000");
        body.put("phoneNumber", "+359555000555");
        body.put("email", email);
        return body;
    }

    @Test
    void getUserReturnsProfile() throws Exception {
        String token = signUpUniqueUser();
        Long userId = userIdFromToken(token);
        String username = userRepository.findById(userId).get().getUsername();

        mockMvc.perform(get("/api/users/" + userId)
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.city").value("Sofia"));
    }

    @Test
    void getUserWithoutTokenIsRejected() throws Exception {
        String token = signUpUniqueUser();

        mockMvc.perform(get("/api/users/" + userIdFromToken(token)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void editUserUpdatesOwnProfile() throws Exception {
        String token = signUpUniqueUser();
        Long userId = userIdFromToken(token);
        var existing = userRepository.findById(userId).get();

        Map<String, Object> update = userPayload(existing.getUsername(), existing.getEmail(), existing.getUcn());
        update.put("firstName", "Renamed");
        update.put("city", "Burgas");

        mockMvc.perform(patch("/api/users")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/users/" + userId)
                        .header("Authorization", bearer(token)))
                .andExpect(jsonPath("$.firstName").value("Renamed"))
                .andExpect(jsonPath("$.city").value("Burgas"));
    }

    @Test
    void editUserInvalidPayloadReturnsValidationError() throws Exception {
        String token = signUpUniqueUser();

        mockMvc.perform(patch("/api/users")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content("{\"username\": \"x\"}")) // too short + everything else missing
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveUserCreatesNewUser() throws Exception {
        String token = signUpUniqueUser();
        long before = userRepository.count();

        mockMvc.perform(post("/api/users")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                userPayload("saveduser", "saved@example.com", "9999999999"))))
                .andExpect(status().isOk());

        assertThat(userRepository.count()).isEqualTo(before + 1);
        assertThat(userRepository.existsUserByUsername("saveduser")).isTrue();
    }

    @Test
    void deleteUserRemovesAccount() throws Exception {
        String token = signUpUniqueUser();
        Long userId = userIdFromToken(token);

        mockMvc.perform(delete("/api/users/" + userId)
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk());

        assertThat(userRepository.findById(userId)).isEmpty();
    }
}
