package com.auctioneer.service.ad;

import com.auctioneer.domain.entities.Ad;
import com.auctioneer.domain.entities.LastBidder;
import com.auctioneer.domain.entities.Status;
import com.auctioneer.domain.entities.User;
import com.auctioneer.dtos.ad.AdDto;
import com.auctioneer.dtos.ad.AdFilterDto;
import com.auctioneer.dtos.ad.BidDto;
import com.auctioneer.repository.ad.AdRepository;
import com.auctioneer.repository.user.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    public AdDto get(Long adId) {
        Ad ad = adRepository.findById(adId).orElseThrow();

        AdDto adDto = new AdDto();

        BeanUtils.copyProperties(ad, adDto);

        return adDto;
    }

    public void create(AdDto adDto, Long userId) {
        Ad ad = new Ad();
        ad.setAuthorId(userId);

        BeanUtils.copyProperties(adDto, ad);

        adRepository.save(ad);
    }

    public List<AdDto> getMyAds(Long authorId) {
        List<Ad> ads = adRepository.findAdByAuthorId(authorId);

        List<AdDto> adDtoList = new ArrayList<>();

        for (Ad ad : ads) {
            AdDto adDto = new AdDto();
            BeanUtils.copyProperties(ad, adDto);
            adDtoList.add(adDto);
        }

        return adDtoList;
    }

    public void edit(Long adId, AdDto adDto) {
        Ad existingAd = adRepository.findById(adId).orElseThrow(
                () -> new IllegalArgumentException("Ad with id " + adId + " not found")
        );

        BeanUtils.copyProperties(adDto, existingAd, "id");

        adRepository.save(existingAd);
    }

    public void bid(Long adId, Long userId, BidDto bidDto) {
        Ad existingAd = adRepository.findById(adId).orElseThrow(
                () -> new IllegalArgumentException("Ad with id " + adId + " not found")
        );

        User lastBidderData = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("User with id " + userId + " not found")
        );

        String username = lastBidderData.getUsername();
        String email = lastBidderData.getEmail();

        BigDecimal amount = bidDto.getAmount();

        if (amount.compareTo(existingAd.getCurrentBidPrice()) <= 0) {
            throw new IllegalArgumentException("Bid amount must be higher than current bid price");
        }

        existingAd.setCurrentBidPrice(amount);

        LastBidder lastBidder = new LastBidder();

        lastBidder.setId(userId);
        lastBidder.setUsername(username);
        lastBidder.setEmail(email);

        existingAd.getLastBidders().add(lastBidder);

        adRepository.save(existingAd);
    }

    public void updateStatus() {
        List<Ad> ads = adRepository.findAdByStatus(Status.INACTIVE);

        ads.forEach(ad -> {
            ad.setStatus(Status.ACTIVE);
        });

        adRepository.saveAll(ads);
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

        cq.select(root)
                .where(predicates.toArray(new Predicate[0]));

        TypedQuery<Ad> query = entityManager.createQuery(cq);

        // Apply pagination
        query.setFirstResult((filter.getPage() - 1) * filter.getSize());
        query.setMaxResults(filter.getSize());

        List<Ad> ads = query.getResultList();

        List<AdDto> adDtoList = new ArrayList<>();

        for (Ad ad : ads) {
            AdDto adDto = new AdDto();
            BeanUtils.copyProperties(ad, adDto);

            adDtoList.add(adDto);
        }

        return adDtoList;
    }
}


