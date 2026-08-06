package com.auctioneer.integration;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdIntegrationTest extends IntegrationTestBase {

    @Test
    void createAd_appliesDefaultsAndPersists() throws Exception {
        String token = signUpUniqueUser();

        Long adId = createAd(token, adPayload("Vintage Clock"));

        JsonNode ad = readJson(mockMvc.perform(get("/api/ads/" + adId))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString());

        assertThat(ad.get("title").asText()).isEqualTo("Vintage Clock");
        // Defaults applied by AdService.create
        assertThat(ad.get("currentBidPrice").decimalValue()).isEqualByComparingTo("100");
        assertThat(ad.get("status").asText()).isEqualTo("ACTIVE");
        assertThat(ad.get("isActive").asBoolean()).isTrue();
        assertThat(ad.get("startingDate").asText()).isEqualTo(LocalDate.now().toString());
        assertThat(ad.get("authorId").asLong()).isEqualTo(userIdFromToken(token));
    }

    @Test
    void createAd_withoutToken_isRejected() throws Exception {
        mockMvc.perform(post("/api/ads/create")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adPayload("No Auth Ad"))))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void createAd_invalidPayload_returnsValidationError() throws Exception {
        String token = signUpUniqueUser();
        Map<String, Object> bad = adPayload("Bad Ad");
        bad.remove("bidStep");
        bad.put("description", "short"); // under 10 chars

        mockMvc.perform(post("/api/ads/create")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void getAd_isPubliclyAccessible() throws Exception {
        String token = signUpUniqueUser();
        Long adId = createAd(token, adPayload("Public Ad"));

        mockMvc.perform(get("/api/ads/" + adId)) // no Authorization header
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Public Ad"));
    }

    @Test
    void getAd_unknownId_returnsNotFound() throws Exception {
        mockMvc.perform(get("/api/ads/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void myAds_returnsOnlyOwnAds() throws Exception {
        String tokenA = signUpUniqueUser();
        String tokenB = signUpUniqueUser();
        createAd(tokenA, adPayload("Ad of A"));
        createAd(tokenB, adPayload("Ad of B"));

        List<JsonNode> myAds = readJsonList(mockMvc.perform(get("/api/ads/my-ads")
                        .header("Authorization", bearer(tokenA)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString());

        assertThat(myAds).hasSize(1);
        assertThat(myAds.get(0).get("title").asText()).isEqualTo("Ad of A");
    }

    @Test
    void editAd_updatesFields() throws Exception {
        String token = signUpUniqueUser();
        Long adId = createAd(token, adPayload("Original Title"));

        Map<String, Object> updated = adPayload("Updated Title");
        updated.put("currentBidPrice", 100);
        updated.put("isActive", true);
        updated.put("status", "ACTIVE");

        mockMvc.perform(post("/api/ads/edit/" + adId)
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/ads/" + adId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    void editAd_unknownId_returnsBadRequest() throws Exception {
        String token = signUpUniqueUser();

        mockMvc.perform(post("/api/ads/edit/999999")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adPayload("Ghost Ad"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void pagination_filtersByActiveFlag() throws Exception {
        String token = signUpUniqueUser();
        createAd(token, adPayload("Active One"));
        createAd(token, adPayload("Active Two"));
        Map<String, Object> inactive = adPayload("Inactive One");
        inactive.put("isActive", false);
        createAd(token, inactive);

        List<JsonNode> activeAds = readJsonList(mockMvc.perform(
                        get("/api/ads/pagination").param("active", "true"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString());

        assertThat(activeAds).hasSize(2);
        assertThat(activeAds).allMatch(ad -> ad.get("isActive").asBoolean());
    }

    @Test
    void pagination_respectsPageSize() throws Exception {
        String token = signUpUniqueUser();
        for (int i = 1; i <= 3; i++) {
            createAd(token, adPayload("Paged Ad " + i));
        }

        List<JsonNode> page = readJsonList(mockMvc.perform(
                        get("/api/ads/pagination").param("page", "1").param("size", "2"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString());

        assertThat(page).hasSize(2);
    }

    // ---------- Bidding ----------

    @Test
    void bid_happyPath_updatesPriceAndDebitsWallet() throws Exception {
        String seller = signUpUniqueUser();
        String bidder = signUpUniqueUser();
        Long adId = createAd(seller, adPayload("Bid Target"));
        addCredits(bidder, new BigDecimal("500"));

        JsonNode response = readJson(mockMvc.perform(post("/api/ads/bid/" + adId)
                        .header("Authorization", bearer(bidder))
                        .contentType(APPLICATION_JSON)
                        .content("{\"amount\": 150}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString());

        assertThat(response.get("currentBidPrice").decimalValue()).isEqualByComparingTo("150");
        assertThat(response.get("nextMinimumBid").decimalValue()).isEqualByComparingTo("160");
        assertThat(response.get("latestBidderUserId").asLong()).isEqualTo(userIdFromToken(bidder));

        // Bid amount was debited from the bidder's wallet
        JsonNode balance = readJson(mockMvc.perform(get("/api/wallet/balance")
                        .header("Authorization", bearer(bidder)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString());
        assertThat(balance.get("balance").decimalValue()).isEqualByComparingTo("350");

        // Ad state reflects the bid (AdDto exposes lastBidders but not latestBidderUserId)
        mockMvc.perform(get("/api/ads/" + adId))
                .andExpect(jsonPath("$.currentBidPrice").value(150.0))
                .andExpect(jsonPath("$.lastBidders.length()").value(1));
    }

    @Test
    void bid_withoutAmount_usesCurrentPricePlusBidStep() throws Exception {
        String seller = signUpUniqueUser();
        String bidder = signUpUniqueUser();
        Long adId = createAd(seller, adPayload("Auto Bid Ad"));
        addCredits(bidder, new BigDecimal("500"));

        JsonNode response = readJson(mockMvc.perform(post("/api/ads/bid/" + adId)
                        .header("Authorization", bearer(bidder))
                        .contentType(APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString());

        // starting price 100 + bid step 10
        assertThat(response.get("currentBidPrice").decimalValue()).isEqualByComparingTo("110");
    }

    @Test
    void bid_outbid_refundsPreviousBidder() throws Exception {
        String seller = signUpUniqueUser();
        String firstBidder = signUpUniqueUser();
        String secondBidder = signUpUniqueUser();
        Long adId = createAd(seller, adPayload("Refund Ad"));
        addCredits(firstBidder, new BigDecimal("500"));
        addCredits(secondBidder, new BigDecimal("500"));

        mockMvc.perform(post("/api/ads/bid/" + adId)
                        .header("Authorization", bearer(firstBidder))
                        .contentType(APPLICATION_JSON)
                        .content("{\"amount\": 150}"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/ads/bid/" + adId)
                        .header("Authorization", bearer(secondBidder))
                        .contentType(APPLICATION_JSON)
                        .content("{\"amount\": 200}"))
                .andExpect(status().isOk());

        // First bidder got their 150 back
        JsonNode firstBalance = readJson(mockMvc.perform(get("/api/wallet/balance")
                        .header("Authorization", bearer(firstBidder)))
                .andReturn().getResponse().getContentAsString());
        assertThat(firstBalance.get("balance").decimalValue()).isEqualByComparingTo("500");

        // Second bidder paid 200
        JsonNode secondBalance = readJson(mockMvc.perform(get("/api/wallet/balance")
                        .header("Authorization", bearer(secondBidder)))
                .andReturn().getResponse().getContentAsString());
        assertThat(secondBalance.get("balance").decimalValue()).isEqualByComparingTo("300");
    }

    @Test
    void bid_onOwnAd_isRejected() throws Exception {
        String seller = signUpUniqueUser();
        Long adId = createAd(seller, adPayload("Own Ad"));
        addCredits(seller, new BigDecimal("500"));

        mockMvc.perform(post("/api/ads/bid/" + adId)
                        .header("Authorization", bearer(seller))
                        .contentType(APPLICATION_JSON)
                        .content("{\"amount\": 150}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("You cannot bid on your own ad"));
    }

    @Test
    void bid_notAboveCurrentPrice_isRejected() throws Exception {
        String seller = signUpUniqueUser();
        String bidder = signUpUniqueUser();
        Long adId = createAd(seller, adPayload("Low Bid Ad"));
        addCredits(bidder, new BigDecimal("500"));

        mockMvc.perform(post("/api/ads/bid/" + adId)
                        .header("Authorization", bearer(bidder))
                        .contentType(APPLICATION_JSON)
                        .content("{\"amount\": 100}")) // equal to current price
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bid amount must be higher than current bid price"));
    }

    @Test
    void bid_withInsufficientBalance_isRejected() throws Exception {
        String seller = signUpUniqueUser();
        String bidder = signUpUniqueUser();
        Long adId = createAd(seller, adPayload("Expensive Ad"));
        addCredits(bidder, new BigDecimal("50")); // less than the 150 bid

        mockMvc.perform(post("/api/ads/bid/" + adId)
                        .header("Authorization", bearer(bidder))
                        .contentType(APPLICATION_JSON)
                        .content("{\"amount\": 150}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Insufficient balance to place bid"));
    }

    @Test
    void bid_withoutToken_isRejected() throws Exception {
        String seller = signUpUniqueUser();
        Long adId = createAd(seller, adPayload("Protected Bid Ad"));

        mockMvc.perform(post("/api/ads/bid/" + adId)
                        .contentType(APPLICATION_JSON)
                        .content("{\"amount\": 150}"))
                .andExpect(status().is4xxClientError());
    }

    // ---------- Expiry ----------

    @Test
    void closeExpiredAds_closesOnlyPastEndDateAds() throws Exception {
        String token = signUpUniqueUser();

        Map<String, Object> expired = adPayload("Expired Ad");
        expired.put("endDate", LocalDate.now().minusDays(1).toString());
        Long expiredId = createAd(token, expired);

        Map<String, Object> current = adPayload("Current Ad");
        current.put("endDate", LocalDate.now().plusDays(7).toString());
        Long currentId = createAd(token, current);

        // Public pub/sub endpoint, no auth required
        String closedCount = mockMvc.perform(post("/api/ads/close-expired"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThat(Integer.parseInt(closedCount)).isEqualTo(1);

        mockMvc.perform(get("/api/ads/" + expiredId))
                .andExpect(jsonPath("$.status").value("CLOSED"))
                .andExpect(jsonPath("$.isActive").value(false));

        mockMvc.perform(get("/api/ads/" + currentId))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.isActive").value(true));
    }

    // ---------- SSE streams ----------

    @Test
    void adBidStream_isPubliclyAccessible() throws Exception {
        String token = signUpUniqueUser();
        Long adId = createAd(token, adPayload("Streamed Ad"));

        mockMvc.perform(get("/api/ads/" + adId + "/stream"))
                .andExpect(status().isOk());
    }

    @Test
    void globalBidStream_isPubliclyAccessible() throws Exception {
        mockMvc.perform(get("/api/ads/stream"))
                .andExpect(status().isOk());
    }
}
