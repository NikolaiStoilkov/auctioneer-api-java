package com.auctioneer.service.discordNotifications;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@RequiredArgsConstructor
public class DiscordService {

    // Still working on it
    private final String webhookUrl = "https://discord.com/api/webhooks/1497237200251654325/MwMWQGPpFIYdyVuu9ZBCy_1bzcrJPUOuRmcMUOfcp0r4TIHrmukKpUs76CM9-QAJkafQ";

    public void sendNotification(String message) {
        try {
            // Discord expects a JSON object with a "content" field
            String jsonPayload = "{\"content\": \"" + message + "\"}";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(webhookUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        System.out.println("Discord Response Code: " + response.statusCode());
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
