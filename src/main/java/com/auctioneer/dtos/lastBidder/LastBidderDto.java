package com.auctioneer.dtos.lastBidder;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/** A single entry in an ad's bid history (bidder, amount, timestamp). */
public class LastBidderDto {
    @NotNull(message = "ID is required")
    private String id;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;
}
