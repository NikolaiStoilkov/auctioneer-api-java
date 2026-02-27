package com.auctioneer.dtos.ad;

import com.auctioneer.domain.entities.Image;
import com.auctioneer.domain.entities.LastBidder;
import com.auctioneer.domain.entities.User;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class AdDto {
    private String title;
    private String image;
    private String description;
    private BigDecimal bidStep;
    private BigDecimal startingBidPrice;
    private BigDecimal currentBidPrice;
    private Long authorId;
    private List<LastBidder> lastBidders;
    private String location;
    private List<String> images;
}
