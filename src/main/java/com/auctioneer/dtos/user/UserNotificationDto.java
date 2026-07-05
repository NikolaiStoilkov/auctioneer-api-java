package com.auctioneer.dtos.user;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class UserNotificationDto {
    private String type;
    private Long adId;
    private String adTitle;
    private BigDecimal currentBidPrice;
    private String latestBidderUsername;
    private String timestamp;
}
