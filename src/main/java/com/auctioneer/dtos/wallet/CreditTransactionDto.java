package com.auctioneer.dtos.wallet;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
/** API representation of a wallet ledger entry. */
public class CreditTransactionDto {
    private Long id;
    private BigDecimal amount;
    private String type;
    private String description;
    private LocalDateTime createdAt;
}

