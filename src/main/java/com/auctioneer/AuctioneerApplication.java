package com.auctioneer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Application entry point. Enables web security, transaction management,
 * scheduling (for the ad activation/close jobs), async execution (for
 * notifications) and JPA auditing (for created/updated timestamps).
 */
@SpringBootApplication
@EnableWebSecurity
@EnableTransactionManagement
@EnableScheduling
@EnableAsync
@EnableJpaAuditing
public class AuctioneerApplication {
    /**
     * Boots the Spring application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(AuctioneerApplication.class, args);
    }
}
