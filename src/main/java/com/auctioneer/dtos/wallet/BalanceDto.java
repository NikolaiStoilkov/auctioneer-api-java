package com.auctioneer.dtos.wallet;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
/** A user's wallet balance and available credits. */
public class BalanceDto {
    private BigDecimal balance;
    private BigDecimal credits;
}

