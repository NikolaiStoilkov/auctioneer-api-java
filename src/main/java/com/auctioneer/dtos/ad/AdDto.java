package com.auctioneer.dtos.ad;

import com.auctioneer.domain.entities.Image;
import com.auctioneer.domain.entities.User;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class AdDto {
    private String title;
    private Image image;
    private String description;
    private BigDecimal bidStep;
    private BigDecimal startingBidPrice;
    private BigDecimal currentBidPrice;
    private User author;
    private User lastBidder;
    private String location;
    private List<String> images;
}
