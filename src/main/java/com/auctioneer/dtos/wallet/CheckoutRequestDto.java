package com.auctioneer.dtos.wallet;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CheckoutRequestDto {
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "1.00", message = "Minimum purchase is 1.00")
    private BigDecimal amount;
}

