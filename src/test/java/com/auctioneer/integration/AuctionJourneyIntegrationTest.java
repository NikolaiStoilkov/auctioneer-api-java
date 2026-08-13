package com.auctioneer.integration;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * End-to-end journeys that cross several endpoints in a single scenario:
 * full auction lifecycles, wallet reconciliation across participants, and
 * cross-resource consistency. These complement the per-endpoint tests in the
 * other integration suites.
 */
class AuctionJourneyIntegrationTest extends IntegrationTestBase {

    private BigDecimal balanceOf(String token) throws Exception {
        JsonNode balance = readJson(mockMvc.perform(get("/api/wallet/balance")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString());
        return balance.get("balance").decimalValue();
    }

    private BigDecimal placeBid(String token, Long adId, String amount) throws Exception {
        JsonNode response = readJson(mockMvc.perform(post("/api/ads/" + adId + "/bids")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content("{\"amount\": " + amount + "}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString());
        return response.get("currentBidPrice").decimalValue();
    }

    private void comment(String token, Long adId, Long authorId, String content) throws Exception {
        mockMvc.perform(post("/api/comments")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Map.of("content", content, "adId", adId, "authorId", authorId))))
                .andExpect(status().isOk());
    }

    private List<JsonNode> commentsOf(Long adId) throws Exception {
        return readJsonList(mockMvc.perform(get("/api/comments/" + adId))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString());
    }

    /**
     * Seller lists an ad; two funded bidders trade the lead through three bids.
     * At the end the ad reflects the winning bid and full history, and every
     * wallet reconciles: only the current leader is out of pocket, every
     * outbid bidder is made whole, and the seller is untouched (no settlement
     * happens at bid time).
     */
    @Test
    void fullAuctionLifecycleThreeBiddersReconcilesWalletsAndHistory() throws Exception {
        String seller = signUpUniqueUser();
        String alice = signUpUniqueUser();
        String bob = signUpUniqueUser();
        Long adId = createAd(seller, adPayload("Antique Vase"));
        addCredits(alice, new BigDecimal("1000"));
        addCredits(bob, new BigDecimal("1000"));

        placeBid(alice, adId, "150");           // Alice leads at 150
        placeBid(bob, adId, "200");             // Bob outbids -> Alice refunded
        placeBid(alice, adId, "250");           // Alice retakes the lead -> Bob refunded

        // Ad reflects the winning bid and the full three-bid history
        JsonNode ad = readJson(mockMvc.perform(get("/api/ads/" + adId))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString());
        assertThat(ad.get("currentBidPrice").decimalValue()).isEqualByComparingTo("250");
        assertThat(ad.get("lastBidders")).hasSize(3);

        // Wallets reconcile: leader paid, outbid bidder refunded, seller untouched
        assertThat(balanceOf(alice)).isEqualByComparingTo("750"); // 1000 - 250 (current leader)
        assertThat(balanceOf(bob)).isEqualByComparingTo("1000");  // outbid -> fully refunded
        assertThat(balanceOf(seller)).isEqualByComparingTo("0");  // no settlement at bid time
    }

    /**
     * A bidder who is already the highest bidder raises their own bid. Their
     * previous amount is refunded and the new one charged, so the net debit is
     * exactly the latest bid — not the sum of both.
     */
    @Test
    void selfOutbidRefundsOwnPreviousBidNet() throws Exception {
        String seller = signUpUniqueUser();
        String bidder = signUpUniqueUser();
        Long adId = createAd(seller, adPayload("Rare Coin"));
        addCredits(bidder, new BigDecimal("1000"));

        placeBid(bidder, adId, "150");
        placeBid(bidder, adId, "200"); // self-outbid: previous 150 refunded, 200 charged

        assertThat(balanceOf(bidder)).isEqualByComparingTo("800"); // 1000 - 200, not 1000 - 350

        JsonNode ad = readJson(mockMvc.perform(get("/api/ads/" + adId))
                .andReturn().getResponse().getContentAsString());
        assertThat(ad.get("currentBidPrice").decimalValue()).isEqualByComparingTo("200");
        assertThat(ad.get("lastBidders")).hasSize(2);
    }

    /**
     * A single buyer journey touching four resources: the seller lists an ad,
     * the buyer funds their wallet, comments, and bids — then every read
     * endpoint reflects the new state consistently.
     */
    @Test
    void buyerJourneySignupCreateAdCommentAndBidAllReflected() throws Exception {
        String seller = signUpUniqueUser();
        String buyer = signUpUniqueUser();
        Long buyerId = userIdFromToken(buyer);
        Long adId = createAd(seller, adPayload("Vintage Camera"));
        addCredits(buyer, new BigDecimal("500"));

        comment(buyer, adId, buyerId, "Is the shutter working?");
        placeBid(buyer, adId, "180");

        // Public ad view reflects the bid
        mockMvc.perform(get("/api/ads/" + adId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentBidPrice").value(180.0))
                .andExpect(jsonPath("$.lastBidders.length()").value(1));

        // Public comments view reflects the comment with the author's username
        List<JsonNode> comments = commentsOf(adId);
        assertThat(comments).hasSize(1);
        assertThat(comments.get(0).get("content").asText()).isEqualTo("Is the shutter working?");
        assertThat(comments.get(0).get("authorUsername").asText())
                .isEqualTo(userRepository.findById(buyerId).get().getUsername());

        // The seller's own listing reflects the raised price
        List<JsonNode> myAds = readJsonList(mockMvc.perform(get("/api/ads/my-ads")
                        .header("Authorization", bearer(seller)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString());
        assertThat(myAds).hasSize(1);
        assertThat(myAds.get(0).get("currentBidPrice").decimalValue()).isEqualByComparingTo("180");
    }

    /**
     * Comments belong to a single ad — reading one ad's comments never leaks
     * comments left on another ad.
     */
    @Test
    void commentsAreScopedToTheirAd() throws Exception {
        String token = signUpUniqueUser();
        Long userId = userIdFromToken(token);
        Long firstAd = createAd(token, adPayload("First Ad"));
        Long secondAd = createAd(token, adPayload("Second Ad"));

        comment(token, firstAd, userId, "Comment on first");
        comment(token, secondAd, userId, "Comment on second");

        List<JsonNode> firstComments = commentsOf(firstAd);
        assertThat(firstComments).hasSize(1);
        assertThat(firstComments.get(0).get("content").asText()).isEqualTo("Comment on first");

        List<JsonNode> secondComments = commentsOf(secondAd);
        assertThat(secondComments).hasSize(1);
        assertThat(secondComments.get(0).get("content").asText()).isEqualTo("Comment on second");
    }

    /**
     * Requesting a page past the end of the result set returns an empty list
     * rather than an error.
     */
    @Test
    void paginationBeyondLastPageReturnsEmptyList() throws Exception {
        String token = signUpUniqueUser();
        createAd(token, adPayload("Only Ad One"));
        createAd(token, adPayload("Only Ad Two"));

        List<JsonNode> farPage = readJsonList(mockMvc.perform(get("/api/ads/pagination")
                        .param("page", "5")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString());

        assertThat(farPage).isEmpty();
    }

    /**
     * Editing a profile only affects the caller (the endpoint edits the
     * authenticated principal), leaving other users' profiles untouched.
     */
    @Test
    void editingOwnProfileDoesNotAffectAnotherUser() throws Exception {
        String alice = signUpUniqueUser();
        String bob = signUpUniqueUser();
        Long aliceId = userIdFromToken(alice);
        Long bobId = userIdFromToken(bob);

        var aliceEntity = userRepository.findById(aliceId).get();
        Map<String, Object> update = userPayload(
                aliceEntity.getUsername(), aliceEntity.getEmail(), aliceEntity.getUcn());
        update.put("firstName", "AliceRenamed");

        mockMvc.perform(patch("/api/users")
                        .header("Authorization", bearer(alice))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk());

        // Alice's own profile changed
        mockMvc.perform(get("/api/users/" + aliceId).header("Authorization", bearer(alice)))
                .andExpect(jsonPath("$.firstName").value("AliceRenamed"));

        // Bob's profile is untouched
        mockMvc.perform(get("/api/users/" + bobId).header("Authorization", bearer(bob)))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    /** Complete valid UserDto payload (edit requires every validated field). */
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
}
