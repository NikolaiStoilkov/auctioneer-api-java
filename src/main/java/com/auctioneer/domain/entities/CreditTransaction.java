package com.auctioneer.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * A single wallet ledger entry — a purchase, debit or refund of credits.
 * The created timestamp is managed automatically by JPA auditing; the
 * optional Stripe session id supports idempotent webhook handling.
 */
@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CREDIT_TRANSACTIONS")
@EntityListeners(AuditingEntityListener.class)
public class CreditTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransactionType type;

    @Column(length = 255)
    private String description;

    @Column(name = "stripe_session_id", unique = true)
    private String stripeSessionId;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public enum TransactionType {
        PURCHASE,
        DEBIT,
        REFUND
    }
}

