package com.auctioneer.controller.ad;

import com.auctioneer.dtos.ad.AdDto;
import com.auctioneer.dtos.user.UserPrincipal;

import com.auctioneer.service.ad.AdService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ads")
@RequiredArgsConstructor
public class AdController {
    private final AdService adService;

    @GetMapping("/{id}")
    private AdDto get(@PathVariable Long id) {
        return adService.get(id);
    }

    @PostMapping("/create")
    private void create(@Valid @RequestBody AdDto ad, @AuthenticationPrincipal UserPrincipal principal) {
        adService.create(ad, principal.getId());
    }
}
