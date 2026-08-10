package com.auctioneer.schedule;

import com.auctioneer.service.ad.AdService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Daily job that closes ads past their end date by delegating to
 * {@link AdService#closeExpiredAds()}.
 */
@Component
@RequiredArgsConstructor
public class CloseExpiredAds {
    private final AdService adService;

    /**
     * Closes expired ads. Runs at 01:00 every day.
     */
    @Scheduled(cron = "0 00 01 * * ?")
    public void closeExpiredAds() {
        adService.closeExpiredAds();
    }
}
