package com.auctioneer.schedule;

import com.auctioneer.service.ad.AdService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActivateAds {
    private final AdService adService;

    // Runs at 06:00 AM every day
    @Scheduled(cron = "0 00 06 * * ?")
    public void activateNewAds() {
        adService.updateStatus();
    }
}
