package com.auctioneer.controller.ad;

import com.auctioneer.dtos.ad.AdFilterDto;
import com.auctioneer.dtos.ad.AdRequestDto;
import com.auctioneer.dtos.ad.AdResponseDto;
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

    /**
     * Returns a single ad by its id.
     *
     * @param id the id of the ad
     * @return the ad
     */
    @GetMapping("/{id}")
    public AdResponseDto get(@PathVariable Long id) {
        return adService.get(id);
    }

    /**
     * Returns all ads created by the authenticated user.
     *
     * @param principal the authenticated user
     * @return the user's ads
     */
    @GetMapping("/my-ads")
    public List<AdResponseDto> getMyAds(@AuthenticationPrincipal UserPrincipal principal) {
        return adService.getMyAds(principal.getId());
    }

    /**
     * Creates a new ad owned by the authenticated user.
     *
     * @param ad        the ad to create
     * @param principal the authenticated user
     */
    @PostMapping
    public void create(@Valid @RequestBody AdRequestDto ad, @AuthenticationPrincipal UserPrincipal principal) {
        adService.create(ad, principal.getId());
    }

    /**
     * Updates an existing ad.
     *
     * @param adId the id of the ad to update
     * @param ad   the new ad data
     */
    @PutMapping("/{adId}")
    public void edit(@PathVariable Long adId, @Valid @RequestBody AdRequestDto ad) {
        adService.edit(adId, ad);
    }

    /**
     * Places a bid on an ad – the request body must contain the bid amount.
     *
     * @param adId      the id of the ad to bid on
     * @param bidDto    the bid, including the amount
     * @param principal the authenticated user placing the bid
     * @return the result of the bid
     */
    @PostMapping("/{adId}/bids")
    public BidResponseDto bid(@PathVariable Long adId, @Valid @RequestBody BidDto bidDto,
                              @AuthenticationPrincipal UserPrincipal principal) {
        return adService.bid(adId, principal.getId(), bidDto);
    }

    /**
     * SSE per-ad live bid stream.
     *
     * @param adId the id of the ad to stream bids for
     * @return an emitter that pushes bid events for the ad
     */
    @GetMapping(value = "/{adId}/stream", produces = "text/event-stream")
    public SseEmitter stream(@PathVariable Long adId) {
        return bidSseService.subscribe(adId);
    }

    /**
     * SSE global stream – broadcasts every bid across all ads.
     *
     * @return an emitter that pushes every bid event
     */
    @GetMapping(value = "/stream", produces = "text/event-stream")
    public SseEmitter globalStream() {
        return bidSseService.subscribeGlobal();
    }

    /**
     * Returns a page of ads matching the given filter.
     *
     * @param filter paging and filtering criteria
     * @return the matching ads
     */
    @GetMapping("/pagination")
    public List<AdResponseDto> getAdsByPagination(@ModelAttribute AdFilterDto filter) {
        return adService.pagination(filter);
    }

    /**
     * Pub/sub push endpoint — closes all active ads whose end date is in the past.
     * Also triggered internally by the daily 01:00 AM scheduler.
     *
     * @return the number of ads that were closed
     */
    @PostMapping("/close-expired")
    public int closeExpiredAds() {
        return adService.closeExpiredAds();
    }
}
