package com.auctioneer.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "LAST_BIDDERS")
public class LastBidder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The user ID of the bidder — used to refund them if outbid. */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Email
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "bid_timestamp")
    private OffsetDateTime timestamp;
}
