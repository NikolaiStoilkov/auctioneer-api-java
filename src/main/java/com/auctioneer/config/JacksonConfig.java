package com.auctioneer.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Central Jackson configuration: ignore unknown properties, write dates as
 * ISO strings rather than timestamps, and map unknown enum values to
 * {@code null} instead of failing the request.
 */
@Configuration
public class JacksonConfig {
    /**
     * Builds the application-wide {@link ObjectMapper}.
     *
     * @return the configured object mapper
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                // Unknown enum values deserialize to null instead of failing the request
                .enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);

        return mapper;
    }
}
