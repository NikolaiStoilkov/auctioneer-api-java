package com.auctioneer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Async infrastructure. Notification work (SSE pushes, Discord webhooks) runs
 * off the request thread on a virtual-thread-per-task executor so a slow or
 * unreachable webhook never blocks the caller.
 */
@Configuration
public class AsyncConfig {

    /**
     * Executor backing {@code @Async("notificationExecutor")} methods — one
     * virtual thread per submitted task.
     *
     * @return the notification executor
     */
    @Bean(name = "notificationExecutor")
    public Executor notificationExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }
}
