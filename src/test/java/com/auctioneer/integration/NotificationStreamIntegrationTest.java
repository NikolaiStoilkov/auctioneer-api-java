package com.auctioneer.integration;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NotificationStreamIntegrationTest extends IntegrationTestBase {

    @Test
    void notificationStream_withValidToken_opensSseConnection() throws Exception {
        String token = signUpUniqueUser();

        mockMvc.perform(get("/api/users/notifications/stream")
                        .param("token", token))
                .andExpect(status().isOk());
    }

    @Test
    void notificationStream_withGarbageToken_fails() throws Exception {
        mockMvc.perform(get("/api/users/notifications/stream")
                        .param("token", "not-a-jwt"))
                .andExpect(status().is5xxServerError());
    }
}
