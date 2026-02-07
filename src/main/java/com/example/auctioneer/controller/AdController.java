package com.example.auctioneer.controller;

import com.example.auctioneer.domain.entities.Ad;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ads")
public class AdController {

    @GetMapping("/{id}")
    private Ad getAd(@PathVariable Long id){
        Ad ad = new Ad();

        ad.setId(1L);

        ad.setTitle("Ad title");
        ad.setDescription("Ad description");

        return ad;
    }
}
