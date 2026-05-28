package com.auctioneer.service.discordNotifications;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response ->
                            System.out.println("Discord [" + webhookUrl + "] -> " + response.statusCode()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendAdNotification(String message)      { send(adsWebhook, message); }
    public void sendBidNotification(String message)     { send(bidsWebhook, message); }
    public void sendCommentNotification(String message) { send(commentsWebhook, message); }
    public void sendUserNotification(String message)    { send(usersWebhook, message); }
    public void sendWalletNotification(String message)  { send(walletWebhook, message); }
    public void sendStripeNotification(String message)  { send(stripeWebhook, message); }
}
