package com.example.auctioneer.dtos;

import com.example.auctioneer.domain.entities.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdDto<Ad> {
    private String title;
    private String description;
    private Double bidStep;
    private Double startingBidPrice;
    private Double currentBidPrice;
    private User author;
    private String lastBidder;
    private String location;
    private List<String> images;
}