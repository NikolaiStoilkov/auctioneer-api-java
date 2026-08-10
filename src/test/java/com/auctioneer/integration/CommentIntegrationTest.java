package com.auctioneer.integration;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommentIntegrationTest extends IntegrationTestBase {

    private Map<String, Object> commentPayload(Long adId, Long authorId, String content) {
        return Map.of("content", content, "adId", adId, "authorId", authorId);
    }

    private void createComment(String token, Long adId, Long authorId, String content) throws Exception {
        mockMvc.perform(post("/api/comments")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentPayload(adId, authorId, content))))
                .andExpect(status().isOk());
    }

    private List<JsonNode> getComments(Long adId) throws Exception {
        return readJsonList(mockMvc.perform(get("/api/comments/" + adId))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString());
    }

    @Test
    void createCommentAndReadItBackPubliclyWithAuthorUsername() throws Exception {
        String token = signUpUniqueUser();
        Long userId = userIdFromToken(token);
        Long adId = createAd(token, adPayload("Commented Ad"));

        createComment(token, adId, userId, "Great item!");

        List<JsonNode> comments = getComments(adId); // no auth needed for reading
        assertThat(comments).hasSize(1);
        assertThat(comments.get(0).get("content").asText()).isEqualTo("Great item!");
        assertThat(comments.get(0).get("authorUsername").asText())
                .isEqualTo(userRepository.findById(userId).get().getUsername());
        assertThat(comments.get(0).get("createdAt").isNull()).isFalse();
    }

    @Test
    void createCommentWithoutTokenIsRejected() throws Exception {
        String token = signUpUniqueUser();
        Long adId = createAd(token, adPayload("Locked Ad"));

        mockMvc.perform(post("/api/comments")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                commentPayload(adId, userIdFromToken(token), "no auth"))))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void createCommentBlankContentReturnsValidationError() throws Exception {
        String token = signUpUniqueUser();
        Long adId = createAd(token, adPayload("Validation Ad"));

        mockMvc.perform(post("/api/comments")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content("{\"content\": \"\", \"adId\": " + adId + ", \"authorId\": "
                                + userIdFromToken(token) + "}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void editCommentUpdatesContentAndKeepsCreatedAt() throws Exception {
        String token = signUpUniqueUser();
        Long userId = userIdFromToken(token);
        Long adId = createAd(token, adPayload("Editable Ad"));
        createComment(token, adId, userId, "Original comment");

        JsonNode original = getComments(adId).get(0);
        long commentId = original.get("id").asLong();

        Map<String, Object> edit = new java.util.HashMap<>(commentPayload(adId, userId, "Edited comment"));
        edit.put("id", commentId);

        mockMvc.perform(put("/api/comments")
                        .header("Authorization", bearer(token))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(edit)))
                .andExpect(status().isOk());

        JsonNode updated = getComments(adId).get(0);
        assertThat(updated.get("content").asText()).isEqualTo("Edited comment");
        assertThat(updated.get("createdAt")).isEqualTo(original.get("createdAt"));
    }

    @Test
    void deleteCommentRemovesIt() throws Exception {
        String token = signUpUniqueUser();
        Long userId = userIdFromToken(token);
        Long adId = createAd(token, adPayload("Deletable Ad"));
        createComment(token, adId, userId, "To be deleted");

        long commentId = getComments(adId).get(0).get("id").asLong();

        mockMvc.perform(delete("/api/comments/" + commentId)
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk());

        assertThat(getComments(adId)).isEmpty();
    }

    @Test
    void getCommentsForAdWithoutCommentsReturnsEmptyList() throws Exception {
        String token = signUpUniqueUser();
        Long adId = createAd(token, adPayload("Silent Ad"));

        assertThat(getComments(adId)).isEmpty();
    }
}
