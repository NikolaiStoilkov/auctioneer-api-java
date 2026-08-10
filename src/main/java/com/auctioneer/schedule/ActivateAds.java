package com.auctioneer.schedule;

import com.auctioneer.service.ad.AdService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Daily job that reactivates INACTIVE ads by delegating to
 * {@link AdService#updateStatus()}.
 */
@Component
@RequiredArgsConstructor
public class ActivateAds {
    private final AdService adService;

    /**
     * Reactivates eligible ads. Runs at 06:00 every day.
     */
    @Scheduled(cron = "0 00 06 * * ?")
    public void activateNewAds() {
        adService.updateStatus();
    }
}
