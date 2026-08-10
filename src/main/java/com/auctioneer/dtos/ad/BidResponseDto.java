package com.auctioneer.dtos.ad;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Response returned after a successful bid. The {@code timestamp} is always
 * supplied explicitly by the service from a single shared {@link Instant},
 * so there is no per-instantiation default here.
 */
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
    private Instant timestamp;
}

