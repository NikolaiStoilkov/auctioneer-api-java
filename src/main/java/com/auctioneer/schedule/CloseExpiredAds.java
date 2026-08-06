package com.auctioneer.schedule;

import com.auctioneer.service.ad.AdService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CloseExpiredAds {
    private final AdService adService;

    // Runs at 01:00 AM every day
    @Scheduled(cron = "0 00 01 * * ?")
    public void closeExpiredAds() {
        adService.closeExpiredAds();
    }
}
