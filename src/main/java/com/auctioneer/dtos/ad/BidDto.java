package com.auctioneer.dtos.ad;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BidDto {
    // adId and userId come from the path variable and JWT — not needed in the body.

    /** Optional — if omitted the server auto-computes currentBidPrice + bidStep. */
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;
}
