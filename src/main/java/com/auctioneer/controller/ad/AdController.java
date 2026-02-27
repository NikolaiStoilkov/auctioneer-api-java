package com.auctioneer.controller.ad;

import com.auctioneer.dtos.ad.AdDto;
import com.auctioneer.dtos.ad.BidDto;
import com.auctioneer.dtos.user.UserPrincipal;

import com.auctioneer.service.ad.AdService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/ads")
@RequiredArgsConstructor
public class AdController {
    private final AdService adService;

    @GetMapping("/{id}")
    private AdDto get(@PathVariable Long id) {
        return adService.get(id);
    }

    @GetMapping("/my-ads")
    private List<AdDto> getMyAds(@AuthenticationPrincipal UserPrincipal principal) {
        return adService.getMyAds(principal.getId());
    }

    @PostMapping("/create")
    private void create(@Valid @RequestBody AdDto ad, @AuthenticationPrincipal UserPrincipal principal) {
        adService.create(ad, principal.getId());
    }

    @PostMapping("/edit/{adId}")
    private void edit(@Valid @RequestBody AdDto ad,@AuthenticationPrincipal UserPrincipal principal) {
        adService.edit(ad);
    }

    @PostMapping("/bid/{adId}")
    private void bid(@PathVariable Long adId, @RequestBody BidDto bidDto, @AuthenticationPrincipal UserPrincipal principal) {
        adService.bid(adId, principal.getId(), bidDto);
    }
}
