package com.example.auctioneer.controller;

import com.example.auctioneer.domain.entities.Ad;
import com.example.auctioneer.dtos.AdDto;
import com.example.auctioneer.repository.AdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
