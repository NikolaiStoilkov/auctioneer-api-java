package com.example.auctioneer.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "ADS"
)
public class Ad {
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_code")
    private Image image;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "bid_step", nullable = false)
    private Double bidStep;

    @Column(name = "starting_bid_price", nullable = false)
    private Double startingBidPrice;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "last_bidder_id")
    private User lastBidder;

    @Column(name = "location", length = 100)
    private String location;
}