package com.auctioneer.dtos.wallet;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class BalanceDto {
    private BigDecimal balance;
    private BigDecimal credits;
}

