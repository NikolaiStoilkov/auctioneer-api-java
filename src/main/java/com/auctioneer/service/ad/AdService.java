package com.auctioneer.service.ad;

import com.auctioneer.domain.entities.Ad;
import com.auctioneer.domain.entities.LastBidder;
import com.auctioneer.domain.entities.Status;
import com.auctioneer.domain.entities.User;
import com.auctioneer.dtos.ad.AdDto;
import com.auctioneer.dtos.ad.AdFilterDto;
import com.auctioneer.dtos.ad.BidDto;
import com.auctioneer.dtos.ad.BidResponseDto;
import com.auctioneer.dtos.user.UserNotificationDto;
import com.auctioneer.exceptions.AdNotFoundException;
import com.auctioneer.exceptions.InvalidBidException;
import com.auctioneer.exceptions.UserNotFoundException;
import com.auctioneer.repository.ad.AdRepository;
import com.auctioneer.repository.user.UserRepository;
import com.auctioneer.service.discordNotifications.DiscordService;
import com.auctioneer.service.user.UserSseService;
import com.auctioneer.service.wallet.WalletService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdService {
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;
    private final BidSseService bidSseService;
    private final UserSseService userSseService;
    private final WalletService walletService;
    private final DiscordService discordService;

    public AdDto get(Long adId) {
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new AdNotFoundException(adId));
        AdDto adDto = new AdDto();
        BeanUtils.copyProperties(ad, adDto);
        return adDto;
    }

    public void create(AdDto adDto, Long userId) {
        Ad ad = new Ad();

        BeanUtils.copyProperties(adDto, ad);

        ad.setAuthorId(userId);

        if (ad.getCurrentBidPrice() == null) {
            ad.setCurrentBidPrice(ad.getStartingBidPrice());
        }

        if (ad.getStatus() == null) {
            ad.setStatus(Status.ACTIVE);
        }

        if (ad.getIsActive() == null) {
            ad.setIsActive(true);
        }

        if (ad.getStartingDate() == null) {
            ad.setStartingDate(LocalDate.now());
        }

        adRepository.save(ad);
        discordService.sendAdNotification("🆕 New ad created: **" + ad.getTitle() + "** by user " + userId);
    }

    public List<AdDto> getMyAds(Long authorId) {
        List<Ad> ads = adRepository.findAdByAuthorId(authorId);
        List<AdDto> result = new ArrayList<>();

        for (Ad ad : ads) {
            AdDto dto = new AdDto();
            BeanUtils.copyProperties(ad, dto);
            result.add(dto);
        }

        return result;
    }

    public void edit(Long adId, AdDto adDto) {
        Ad existingAd = adRepository.findById(adId)
                .orElseThrow(() -> new AdNotFoundException(adId));

        // authorId must not be reassigned from the client, and lastBidders is an
        // orphan-removal collection Hibernate does not allow to be replaced
        BeanUtils.copyProperties(adDto, existingAd, "id", "authorId", "lastBidders");

        adRepository.save(existingAd);
        discordService.sendAdNotification("✏️ Ad edited (ID: " + adId + "): **" + existingAd.getTitle() + "**");
    }

    /**
     * Places a bid. The bid amount must be higher than the current bid price.
     */
    @Transactional
    public BidResponseDto bid(Long adId, Long userId, BidDto bidDto) {
        Ad ad = adRepository.findById(adId)
                .orElseThrow(() -> new AdNotFoundException(adId));


        User bidder = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (ad.getAuthorId() != null && ad.getAuthorId().equals(userId)) {
            throw InvalidBidException.ownAdBid();
        }

        BigDecimal bidAmount = (bidDto.getAmount() != null)
                ? bidDto.getAmount()
                : ad.getCurrentBidPrice().add(ad.getBidStep());

        if (bidAmount.compareTo(ad.getCurrentBidPrice()) <= 0) {
            throw InvalidBidException.bidTooLow();
        }

        // Snapshot previous state before any mutation
        BigDecimal previousBidAmount = ad.getCurrentBidPrice();
        Long previousBidderUserId = ad.getLatestBidderUserId();

        // Deduct bid amount from the new bidder (throws if balance insufficient)
        walletService.debit(userId, bidAmount, "Bid on: " + ad.getTitle());

        // Refund the previous highest bidder (including self-outbid)
        if (previousBidderUserId != null) {
            walletService.refund(previousBidderUserId, previousBidAmount,
                    "Outbid refund: " + ad.getTitle());
        }

        // Record bid history
        LastBidder record = new LastBidder();
        record.setUserId(userId);
        record.setUsername(bidder.getUsername());
        record.setEmail(bidder.getEmail());
        record.setAmount(bidAmount);
        record.setTimestamp(OffsetDateTime.now(ZoneOffset.UTC));
        ad.getLastBidders().add(record);

        String nowStr = Instant.now().toString();

        // Notify the previous highest bidder that they were outbid
        if (previousBidderUserId != null && !previousBidderUserId.equals(userId)) {
            userSseService.notifyUser(previousBidderUserId, UserNotificationDto.builder()
                    .type("OUTBID")
                    .adId(adId)
                    .adTitle(ad.getTitle())
                    .currentBidPrice(bidAmount)
                    .latestBidderUsername(bidder.getUsername())
                    .timestamp(nowStr)
                    .build());
        }

        // Notify the ad author about the new bid (if they are not the bidder themselves)
        Long authorId = ad.getAuthorId();
        if (authorId != null && !authorId.equals(userId)) {
            userSseService.notifyUser(authorId, UserNotificationDto.builder()
                    .type("NEW_BID")
                    .adId(adId)
                    .adTitle(ad.getTitle())
                    .currentBidPrice(bidAmount)
                    .latestBidderUsername(bidder.getUsername())
                    .timestamp(nowStr)
                    .build());
        }

        ad.setCurrentBidPrice(bidAmount);
        ad.setLatestBidderUserId(userId);
        adRepository.save(ad);

        BidResponseDto response = BidResponseDto.builder()
                .adId(adId)
                .currentBidPrice(bidAmount)
                .nextMinimumBid(bidAmount.add(ad.getBidStep()))
                .latestBidderUsername(bidder.getUsername())
                .latestBidderUserId(userId)
                .timestamp(Instant.now())
                .build();

        bidSseService.broadcast(adId, response);
        discordService.sendBidNotification("💰 New bid on **" + ad.getTitle() + "** (ID: " + adId + "): **" + bidAmount + "** by " + bidder.getUsername());
        return response;
    }

    public void updateStatus() {
        List<Ad> ads = adRepository.findAdByStatus(Status.INACTIVE);

        ads.forEach(ad -> ad.setStatus(Status.ACTIVE));

        adRepository.saveAll(ads);
        discordService.sendAdNotification("🔄 Scheduled status update ran — " + ads.size() + " ads reactivated");
    }

    /**
     * Closes all active ads whose end date is in the past.
     * Ads get the CLOSED status so the daily reactivation job (which flips
     * INACTIVE ads back to ACTIVE) does not reopen them.
     *
     * @return the number of ads that were closed
     */
    @Transactional
    public int closeExpiredAds() {
        List<Ad> expiredAds = adRepository.findAdByIsActiveTrueAndEndDateBefore(LocalDate.now());

        for (Ad ad : expiredAds) {
            ad.setIsActive(false);
            ad.setStatus(Status.CLOSED);
        }

        adRepository.saveAll(expiredAds);

        if (!expiredAds.isEmpty()) {
            discordService.sendAdNotification("🔒 Scheduled close ran — " + expiredAds.size() + " expired ads closed");
        }

        return expiredAds.size();
    }

    public List<AdDto> pagination(AdFilterDto filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Ad> cq = cb.createQuery(Ad.class);
        Root<Ad> root = cq.from(Ad.class);

        List<Predicate> predicates = new ArrayList<>();
        if (filter.getActive() != null) {
            predicates.add(cb.equal(root.get("isActive"), filter.getActive()));
        }

        if (filter.getDateFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("startingDate"), filter.getDateFrom()));
        }

        if (filter.getDateTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("startingDate"), filter.getDateTo()));
        }

        cq.select(root).where(predicates.toArray(new Predicate[0]));
        TypedQuery<Ad> query = entityManager.createQuery(cq);
        query.setFirstResult((filter.getPage() - 1) * filter.getSize());
        query.setMaxResults(filter.getSize());

        List<AdDto> result = new ArrayList<>();
        for (Ad ad : query.getResultList()) {
            AdDto dto = new AdDto();
            BeanUtils.copyProperties(ad, dto);
            result.add(dto);
        }

        return result;
    }
}
