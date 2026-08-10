package com.auctioneer.integration;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ShippingIntegrationTest extends IntegrationTestBase {

    private Map<String, Object> shippingPayload(String firstName, String phone) {
        Map<String, Object> body = new HashMap<>();
        body.put("firstName", firstName);
        body.put("middleName", "M");
        body.put("lastName", "Receiver");
        body.put("phoneNumber", phone);
        body.put("country", "Bulgaria");
        body.put("city", "Plovdiv");
        body.put("street", "Main Street");
        body.put("streetNumber", "5");
        body.put("postalCode", "4000");
        return body;
    }

    @Test
    void saveShippingAddressPersistsIt() throws Exception {
        String token = signUpUniqueUser();

        mockMvc.perform(post("/api/shipping")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                shippingPayload("Anna", "+359111000111"))))
                .andExpect(status().isOk());

        assertThat(shippingRepository.findAll()).hasSize(1);
        assertThat(shippingRepository.findAll().get(0).getFirstName()).isEqualTo("Anna");
    }

    @Test
    void getShippingAddressReturnsSavedAddress() throws Exception {
        String token = signUpUniqueUser();
        mockMvc.perform(post("/api/shipping")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                shippingPayload("Boris", "+359222000222"))))
                .andExpect(status().isOk());

        Long addressId = shippingRepository.findAll().get(0).getId();

        mockMvc.perform(get("/api/shipping/" + addressId)
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Boris"))
                .andExpect(jsonPath("$.city").value("Plovdiv"));
    }

    @Test
    void editShippingAddressUpdatesFields() throws Exception {
        String token = signUpUniqueUser();
        mockMvc.perform(post("/api/shipping")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                shippingPayload("Carla", "+359333000333"))))
                .andExpect(status().isOk());

        Long addressId = shippingRepository.findAll().get(0).getId();

        Map<String, Object> updated = shippingPayload("Carla", "+359333000333");
        updated.put("city", "Varna");

        mockMvc.perform(patch("/api/shipping/" + addressId)
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/shipping/" + addressId)
                        .header("Authorization", bearer(token)))
                .andExpect(jsonPath("$.city").value("Varna"));
    }

    @Test
    void saveShippingAddressInvalidPayloadReturnsValidationError() throws Exception {
        String token = signUpUniqueUser();
        Map<String, Object> bad = shippingPayload("Dora", "not-a-phone");

        mockMvc.perform(post("/api/shipping")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getShippingAddressUnknownIdReturnsNotFound() throws Exception {
        String token = signUpUniqueUser();

        mockMvc.perform(get("/api/shipping/999999")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shippingEndpointsWithoutTokenAreRejected() throws Exception {
        mockMvc.perform(get("/api/shipping/1"))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(post("/api/shipping")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                shippingPayload("Eve", "+359444000444"))))
                .andExpect(status().is4xxClientError());
    }
}
