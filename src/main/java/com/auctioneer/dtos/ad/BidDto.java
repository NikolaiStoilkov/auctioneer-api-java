package com.auctioneer.dtos.ad;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BidDto {
    private Long adId;
    private Long userId;
    private BigDecimal amount;
}
