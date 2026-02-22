package com.auctioneer.controller;

import com.auctioneer.dtos.AdDto;
import com.auctioneer.dtos.UserPrincipal;
import com.auctioneer.repository.AdRepository;

import com.auctioneer.service.AdService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ads")
@RequiredArgsConstructor
public class AdController {

    private final AdRepository adRepository;
    private final AdService adService;

    @GetMapping("/{id}")
    private AdDto get(@PathVariable Long id) {
        AdDto adDto = new AdDto();

        adRepository.findById(id).ifPresent(ad -> {
            adDto.setTitle(ad.getTitle());
            adDto.setDescription(ad.getDescription());
            adDto.setStartingBidPrice(ad.getStartingBidPrice());
            adDto.setCurrentBidPrice(ad.getCurrentBidPrice());
        });

        return adDto;
    }

    @PostMapping("/create")
    private void create(@Valid @RequestBody AdDto ad, @AuthenticationPrincipal UserPrincipal principal) {
        adService.create(ad, principal.getId());
    }
}
