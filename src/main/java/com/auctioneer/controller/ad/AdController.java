package com.auctioneer.controller.ad;

import com.auctioneer.dtos.ad.AdDto;
import com.auctioneer.dtos.ad.AdFilterDto;
import com.auctioneer.dtos.ad.BidDto;
import com.auctioneer.dtos.ad.BidResponseDto;
import com.auctioneer.dtos.user.UserPrincipal;
import com.auctioneer.service.ad.AdService;
import com.auctioneer.service.ad.BidSseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api/ads")
@RequiredArgsConstructor
public class AdController {
    private final AdService adService;
    private final BidSseService bidSseService;

    @GetMapping("/{id}")
    public AdDto get(@PathVariable Long id) {
        return adService.get(id);
    }

    @GetMapping("/my-ads")
    public List<AdDto> getMyAds(@AuthenticationPrincipal UserPrincipal principal) {
        return adService.getMyAds(principal.getId());
    }

    @PostMapping("/create")
    public void create(@Valid @RequestBody AdDto ad, @AuthenticationPrincipal UserPrincipal principal) {
        adService.create(ad, principal.getId());
    }

    @PostMapping("/edit/{adId}")
    public void edit(@PathVariable Long adId, @Valid @RequestBody AdDto ad) {
        adService.edit(adId, ad);
    }

    /** Bid – the request body must contain the bid amount. */
    @PostMapping("/bid/{adId}")
    public BidResponseDto bid(@PathVariable Long adId, @Valid @RequestBody BidDto bidDto,
                              @AuthenticationPrincipal UserPrincipal principal) {
        return adService.bid(adId, principal.getId(), bidDto);
    }

    /** SSE per-ad live bid stream. */
    @GetMapping(value = "/{adId}/stream", produces = "text/event-stream")
    public SseEmitter stream(@PathVariable Long adId) {
        return bidSseService.subscribe(adId);
    }

    /** SSE global stream – broadcasts every bid across all ads. */
    @GetMapping(value = "/stream", produces = "text/event-stream")
    public SseEmitter globalStream() {
        return bidSseService.subscribeGlobal();
    }

    @GetMapping("/pagination")
    public List<AdDto> getAdsByPagination(@ModelAttribute AdFilterDto filter) {
        return adService.pagination(filter);
    }
}
