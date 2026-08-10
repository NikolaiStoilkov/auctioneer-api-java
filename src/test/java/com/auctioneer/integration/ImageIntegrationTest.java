package com.auctioneer.integration;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ImageIntegrationTest extends IntegrationTestBase {

    private static final byte[] PNG_BYTES = {
            (byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A, 0x01, 0x02, 0x03, 0x04
    };

    @Test
    void uploadThenDownloadRoundTripsBytesAndContentType() throws Exception {
        String token = signUpUniqueUser();

        MockMultipartFile file = new MockMultipartFile(
                "file", "pic.png", MediaType.IMAGE_PNG_VALUE, PNG_BYTES);

        String uploadResponse = mockMvc.perform(multipart("/api/images")
                        .file(file)
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode json = readJson(uploadResponse);
        String code = json.get("code").asText();
        assertThat(code).isNotBlank();

        mockMvc.perform(get("/api/images/" + code))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.IMAGE_PNG))
                .andExpect(content().bytes(PNG_BYTES));
    }

    @Test
    void uploadWithoutTokenIsRejected() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "pic.png", MediaType.IMAGE_PNG_VALUE, PNG_BYTES);

        mockMvc.perform(multipart("/api/images").file(file))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void downloadUnknownCodeReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/images/does-not-exist"))
                .andExpect(status().isNotFound());
    }
}
