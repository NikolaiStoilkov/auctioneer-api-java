package com.auctioneer.domain.entities;

import com.auctioneer.converters.StringListConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "ADS")
@EntityListeners(AuditingEntityListener.class)
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Optimistic-lock guard — concurrent bids on the same ad surface as HTTP 409. */
    @Version
    @Column(name = "version")
    private Long version;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "image", length = 512)
    private String image;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "bid_step", nullable = false)
    private BigDecimal bidStep;

    @Column(name = "starting_bid_price", nullable = false)
    private BigDecimal startingBidPrice;

    @Column(name = "current_bid_price", nullable = false)
    private BigDecimal currentBidPrice;

    @Column(name = "author_id", nullable = false)
    private Long authorId;

    /** Tracks the current highest bidder so they can be refunded when outbid. */
    @Column(name = "latest_bidder_user_id")
    private Long latestBidderUserId;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ad_id")
    private List<LastBidder> lastBidders = new ArrayList<>();

    @Column(name = "location", length = 100)
    private String location;

    @Column(name = "images", columnDefinition = "TEXT")
    @Convert(converter = StringListConverter.class)
    private List<String> images;

    @Column
    private Boolean isActive;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column
    private LocalDate startingDate;

    @Column
    private LocalDate endDate;

    @Column
    private BigDecimal balance;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
