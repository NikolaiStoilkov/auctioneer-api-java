package com.example.auctioneer.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(
        name = "ADS"
)
@Setter
public class Ad {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "title", nullable = false, length = 100)
        private String title;

        @ElementCollection(fetch = FetchType.LAZY)
        @CollectionTable(name = "AD_IMAGES", joinColumns = @JoinColumn(name = "ad_id"))
        @Column(name = "image")
        private List<String> images = new ArrayList<>();

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

        public Ad() {
        }

}

/**
 * Title
 * Images
 * Description
 * Bid step
 * Starting bid price
 * Author
 * Last bidder
 * Location
 */