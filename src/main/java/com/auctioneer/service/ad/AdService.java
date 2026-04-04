package com.auctioneer.service.ad;

import com.auctioneer.domain.entities.Ad;
import com.auctioneer.domain.entities.LastBidder;
import com.auctioneer.domain.entities.Status;
import com.auctioneer.domain.entities.User;
import com.auctioneer.dtos.ad.AdDto;
import com.auctioneer.dtos.ad.AdFilterDto;
import com.auctioneer.dtos.ad.BidDto;
import com.auctioneer.filters.AdFilter;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
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

        List<AdDto> adDtoList = Collections.singletonList(new AdDto());

        BeanUtils.copyProperties(ads, adDtoList);

        return adDtoList;
    }

    public void edit(AdDto adDto) {
        Ad ad = new Ad();

        BeanUtils.copyProperties(adDto, ad);

        adRepository.save(ad);
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
//        int page = filter.getPage();
//        int size = filter.getSize();
//
//        PageRequest pageRequest = PageRequest.of(page, size); // page = 2, page - 1 = 1
//        Page<Ad> adsPage = adRepository.findPage(pageRequest); // I might need explanation why we need this. In the example we had to fetch them, but technically we don't use it anywhere.

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Ad> cq = cb.createQuery(Ad.class);
        Root<Ad> root = cq.from(Ad.class);

        List<Predicate> predicates = new ArrayList<>();
        if (filter.getActive() != null) {
            predicates.add(cb.equal(root.get(String.valueOf(Status.ACTIVE)), filter.getActive()));
        }

        if (filter.getDateFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("startingDate"), filter.getDateFrom()));
        }

        if (filter.getDateTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("startingDate"), filter.getDateFrom()));
        }

        cq.select(root)
                .where(cb.and(predicates));

        TypedQuery<Ad> query = entityManager.createQuery(cq);

        // Apply pagination
        query.setFirstResult((filter.getPage() - 1) * filter.getSize()); // page = 2, (2 - 1) * 10 = 10
        query.setMaxResults(filter.getSize());

        List<AdDto> adDtoList = new ArrayList<>();

        List<Ad> ads = query.getResultList();

        for (Ad ad : ads) {
            AdDto adDto = new AdDto();

            BeanUtils.copyProperties(ad, adDto);

            adDtoList.add(adDto);
        }

        return adDtoList;
    }
}


