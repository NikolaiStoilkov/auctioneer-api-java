package com.auctioneer.dtos;

import com.auctioneer.domain.entities.User;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class AdDto {
    private String title;
    private String description;
    private BigDecimal bidStep;
    private BigDecimal startingBidPrice;
    private BigDecimal currentBidPrice;
    private String lastBidder;
    private String location;
    private List<String> images;
}
