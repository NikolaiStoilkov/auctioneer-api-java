package com.auctioneer.integration;

import com.auctioneer.repository.ad.AdRepository;
import com.auctioneer.repository.comment.CommentRepository;
import com.auctioneer.repository.user.ShippingRepository;
import com.auctioneer.repository.user.UserRepository;
import com.auctioneer.repository.wallet.CreditTransactionRepository;
import com.auctioneer.service.auth.JwtService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Base class for full-stack integration tests: real Spring context,
 * real security filter chain (JWT), HSQLDB in-memory database.
 * Discord webhooks are blank in test properties, so no external calls are made.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
// @TestPropertySource outranks the root ./application.properties (which points at
// the real MySQL database and real Discord/Stripe keys).
@TestPropertySource(locations = "classpath:application-test.properties")
public abstract class IntegrationTestBase {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected JwtService jwtService;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected AdRepository adRepository;

    @Autowired
    protected CommentRepository commentRepository;

    @Autowired
    protected ShippingRepository shippingRepository;

    @Autowired
    protected CreditTransactionRepository creditTransactionRepository;

    private static final java.util.concurrent.atomic.AtomicLong SEQ = new java.util.concurrent.atomic.AtomicLong(1);

    private int userCounter = 0;

    @BeforeEach
    void cleanDatabase() {
        // Children first to satisfy FK-safe ordering
        commentRepository.deleteAll();
        creditTransactionRepository.deleteAll();
        adRepository.deleteAll(); // cascades LAST_BIDDERS
        shippingRepository.deleteAll();
        userRepository.deleteAll();
    }

    /** Builds a complete, valid sign-up payload. All unique fields get the given suffix. */
    protected Map<String, Object> signUpPayload(String suffix) {
        Map<String, Object> body = new HashMap<>();
        body.put("username", "user" + suffix);
        body.put("password", "password123");
        body.put("firstName", "John");
        body.put("middleName", "M");
        body.put("lastName", "Doe");
        long seq = SEQ.getAndIncrement();
        body.put("ucn", String.format("%010d", seq));
        body.put("country", "Bulgaria");
        body.put("city", "Sofia");
        body.put("street", "Vitosha");
        body.put("streetNumber", "12");
        body.put("postalCode", "1000");
        body.put("phoneNumber", "+359888" + String.format("%06d", seq));
        body.put("email", "user" + suffix + "@example.com");
        body.put("roles", List.of("USER"));
        return body;
    }

    /** Signs up a fresh user and returns their JWT. */
    protected String signUp(String suffix) throws Exception {
        String response = mockMvc.perform(post("/api/auth/sign-up")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpPayload(suffix))))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return response;
    }

    /** Signs up a user with an auto-generated unique suffix and returns their JWT. */
    protected String signUpUniqueUser() throws Exception {
        return signUp("t" + System.nanoTime() % 1_000_000 + "_" + (userCounter++));
    }

    protected Long userIdFromToken(String token) {
        return Long.valueOf(jwtService.extractUserId(token));
    }

    protected String bearer(String token) {
        return "Bearer " + token;
    }

    /** Tops up the wallet of the token's user via the public API. */
    protected void addCredits(String token, BigDecimal amount) throws Exception {
        mockMvc.perform(post("/api/wallet/add-credits")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content("{\"amount\": " + amount + "}"))
                .andExpect(status().isOk());
    }

    /** Complete valid ad payload. */
    protected Map<String, Object> adPayload(String title) {
        Map<String, Object> body = new HashMap<>();
        body.put("title", title);
        body.put("description", "A long enough description for validation");
        body.put("bidStep", 10);
        body.put("startingBidPrice", 100);
        body.put("location", "Sofia");
        return body;
    }

    /** Creates an ad for the token's user and returns its id. */
    protected Long createAd(String token, Map<String, Object> payload) throws Exception {
        mockMvc.perform(post("/api/ads")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk());

        List<JsonNode> myAds = readJsonList(mockMvc.perform(get("/api/ads/my-ads")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString());

        return myAds.stream()
                .filter(ad -> payload.get("title").equals(ad.get("title").asText()))
                .findFirst().orElseThrow()
                .get("id").asLong();
    }

    protected JsonNode readJson(String json) throws Exception {
        return objectMapper.readTree(json);
    }

    protected List<JsonNode> readJsonList(String json) throws Exception {
        JsonNode node = objectMapper.readTree(json);
        return objectMapper.convertValue(node,
                objectMapper.getTypeFactory().constructCollectionType(List.class, JsonNode.class));
    }
}
