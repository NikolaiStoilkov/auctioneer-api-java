package com.auctioneer.service.discordNotifications;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Posts notifications to Discord via incoming webhooks. Every send runs
 * asynchronously on the virtual-thread notification executor, so a slow or
 * unreachable webhook never blocks the request thread.
 */
@Slf4j
@Service
public class DiscordService {

    @Value("${discord.webhook.ads:}")
    private String adsWebhook;

    @Value("${discord.webhook.bids:}")
    private String bidsWebhook;

    @Value("${discord.webhook.comments:}")
    private String commentsWebhook;

    @Value("${discord.webhook.users:}")
    private String usersWebhook;

    @Value("${discord.webhook.wallet:}")
    private String walletWebhook;

    @Value("${discord.webhook.stripe:}")
    private String stripeWebhook;

    private void send(String webhookUrl, String message) {
        if (webhookUrl == null || webhookUrl.isBlank()) return;
        try {
            String safe = message.replace("\\", "\\\\").replace("\"", "\\\"");
            String jsonPayload = "{\"content\": \"" + safe + "\"}";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(webhookUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.debug("Discord webhook responded with status {}", response.statusCode());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Discord notification interrupted", e);
        } catch (Exception e) {
            log.warn("Failed to send Discord notification", e);
        }
    }

    @Async("notificationExecutor")
    public void sendAdNotification(String message)      { send(adsWebhook, message); }

    @Async("notificationExecutor")
    public void sendBidNotification(String message)     { send(bidsWebhook, message); }

    @Async("notificationExecutor")
    public void sendCommentNotification(String message) { send(commentsWebhook, message); }

    @Async("notificationExecutor")
    public void sendUserNotification(String message)    { send(usersWebhook, message); }

    @Async("notificationExecutor")
    public void sendWalletNotification(String message)  { send(walletWebhook, message); }

    @Async("notificationExecutor")
    public void sendStripeNotification(String message)  { send(stripeWebhook, message); }
}
