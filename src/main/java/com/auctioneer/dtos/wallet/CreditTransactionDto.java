package com.auctioneer.dtos.wallet;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class CreditTransactionDto {
    private Long id;
    private BigDecimal amount;
    private String type;
    private String description;
    private LocalDateTime createdAt;
}

