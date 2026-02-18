package com.auctioneer.controller;

import com.auctioneer.domain.entities.Ad;
import com.auctioneer.dtos.AdDto;
import com.auctioneer.repository.AdRepository;

import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ads")
@RequiredArgsConstructor
public class AdController {
    private final AdRepository adRepository;

    @GetMapping("/{id}")
    private AdDto<Ad> get(@PathVariable Long id) {
        AdDto<Ad> adDto = new AdDto<>();

        adRepository.findById(id).ifPresent(ad -> {
            adDto.setTitle(ad.getTitle());
            adDto.setDescription(ad.getDescription());
            adDto.setStartingBidPrice(ad.getStartingBidPrice());
            adDto.setCurrentBidPrice(ad.getCurrentBidPrice());
            adDto.setAuthor(ad.getAuthor());
        });

        return adDto;
    }

    @PostMapping("/create")
    private void create(@RequestBody Ad ad) {
        try {
            adRepository.save(ad);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
