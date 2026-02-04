package com.example.auctioneer.domain.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "ADS"
)
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

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Double getBidStep() {
            return bidStep;
        }

        public void setBidStep(Double bidStep) {
            this.bidStep = bidStep;
        }

        public Double getStartingBidPrice() {
            return startingBidPrice;
        }

        public void setStartingBidPrice(Double startingBidPrice) {
            this.startingBidPrice = startingBidPrice;
        }

        public User getAuthor() {
            return author;
        }

        public void setAuthor(User author) {
            this.author = author;
        }

        public User getLastBidder() {
            return lastBidder;
        }

        public void setLastBidder(User lastBidder) {
            this.lastBidder = lastBidder;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
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