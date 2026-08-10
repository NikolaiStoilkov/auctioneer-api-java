package com.auctioneer.integration;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Endpoints that talk to the real Stripe API (setup-intent, payment-methods, …)
 * are only tested for security here — calling them with a valid token would
 * hit Stripe with the dummy test key.
 */
class StripeIntegrationTest extends IntegrationTestBase {

    @Test
    void configIsPublicAndReturnsPublishableKey() throws Exception {
        mockMvc.perform(get("/api/stripe/config"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.publishableKey").value("pk_test_dummy"));
    }

    @Test
    void setupIntentWithoutTokenIsRejected() throws Exception {
        mockMvc.perform(post("/api/stripe/setup-intent"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void paymentMethodsWithoutTokenIsRejected() throws Exception {
        mockMvc.perform(get("/api/stripe/payment-methods"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void createPaymentIntentWithoutTokenIsRejected() throws Exception {
        mockMvc.perform(post("/api/wallet/create-payment-intent")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 100}"))
                .andExpect(status().is4xxClientError());
    }
}
