package com.auctioneer.service.ad;

import com.auctioneer.domain.entities.Ad;
import com.auctioneer.domain.entities.LastBidder;
import com.auctioneer.domain.entities.User;
import com.auctioneer.dtos.ad.AdDto;
import com.auctioneer.dtos.ad.BidDto;
import com.auctioneer.repository.ad.AdRepository;
import com.auctioneer.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;

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
}


