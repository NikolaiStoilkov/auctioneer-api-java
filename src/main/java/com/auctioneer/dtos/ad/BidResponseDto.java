package com.auctioneer.dtos.ad;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BidResponseDto {
    private Long adId;
    private BigDecimal currentBidPrice;
    private BigDecimal nextMinimumBid;
    private String latestBidderUsername;
    private Long latestBidderUserId;
    @Builder.Default
    private Instant timestamp = Instant.now();
}

