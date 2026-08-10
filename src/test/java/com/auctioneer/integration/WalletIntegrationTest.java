package com.auctioneer.integration;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WalletIntegrationTest extends IntegrationTestBase {

    private JsonNode getBalance(String token) throws Exception {
        return readJson(mockMvc.perform(get("/api/wallet/balance")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString());
    }

    @Test
    void newUserStartsWithZeroBalance() throws Exception {
        String token = signUpUniqueUser();

        JsonNode balance = getBalance(token);
        assertThat(balance.get("balance").decimalValue()).isEqualByComparingTo("0");
        assertThat(balance.get("credits").decimalValue()).isEqualByComparingTo("0");
    }

    @Test
    void addCreditsIncreasesBalanceAndRecordsTransaction() throws Exception {
        String token = signUpUniqueUser();

        JsonNode result = readJson(mockMvc.perform(post("/api/wallet/add-credits")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content("{\"amount\": 250}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString());

        assertThat(result.get("balance").decimalValue()).isEqualByComparingTo("250");
        assertThat(result.get("credits").decimalValue()).isEqualByComparingTo("250");

        JsonNode transactions = readJson(mockMvc.perform(get("/api/wallet/transactions")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString());

        assertThat(transactions.get("content")).hasSize(1);
        JsonNode tx = transactions.get("content").get(0);
        assertThat(tx.get("type").asText()).isEqualTo("PURCHASE");
        assertThat(tx.get("amount").decimalValue()).isEqualByComparingTo("250");
    }

    @Test
    void confirmCreditsAlsoAddsCredits() throws Exception {
        String token = signUpUniqueUser();

        mockMvc.perform(post("/api/wallet/confirm-credits")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content("{\"amount\": 100}"))
                .andExpect(status().isOk());

        assertThat(getBalance(token).get("balance").decimalValue()).isEqualByComparingTo("100");
    }

    @Test
    void addCreditsBelowMinimumReturnsValidationError() throws Exception {
        String token = signUpUniqueUser();

        mockMvc.perform(post("/api/wallet/add-credits")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content("{\"amount\": 0.5}"))
                .andExpect(status().isBadRequest());

        assertThat(getBalance(token).get("balance").decimalValue()).isEqualByComparingTo("0");
    }

    @Test
    void addCreditsMissingAmountReturnsValidationError() throws Exception {
        String token = signUpUniqueUser();

        mockMvc.perform(post("/api/wallet/add-credits")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void transactionsArePaged() throws Exception {
        String token = signUpUniqueUser();
        addCredits(token, new BigDecimal("10"));
        addCredits(token, new BigDecimal("20"));
        addCredits(token, new BigDecimal("30"));

        JsonNode page = readJson(mockMvc.perform(get("/api/wallet/transactions")
                        .header("Authorization", bearer(token))
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString());

        assertThat(page.get("content")).hasSize(2);
        assertThat(page.get("totalElements").asInt()).isEqualTo(3);
    }

    @Test
    void walletBalancesAreIsolatedPerUser() throws Exception {
        String tokenA = signUpUniqueUser();
        String tokenB = signUpUniqueUser();
        addCredits(tokenA, new BigDecimal("100"));

        assertThat(getBalance(tokenA).get("balance").decimalValue()).isEqualByComparingTo("100");
        assertThat(getBalance(tokenB).get("balance").decimalValue()).isEqualByComparingTo("0");
    }

    @Test
    void walletEndpointsWithoutTokenAreRejected() throws Exception {
        mockMvc.perform(get("/api/wallet/balance"))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(post("/api/wallet/add-credits")
                        .contentType(APPLICATION_JSON)
                        .content("{\"amount\": 100}"))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(get("/api/wallet/transactions"))
                .andExpect(status().is4xxClientError());
    }
}
