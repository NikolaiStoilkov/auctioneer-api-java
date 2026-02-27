package com.auctioneer.service.ad;

import com.auctioneer.domain.entities.Ad;
import com.auctioneer.domain.entities.LastBidder;
import com.auctioneer.domain.entities.User;
import com.auctioneer.dtos.ad.AdDto;
import com.auctioneer.dtos.ad.BidDto;
import com.auctioneer.repository.ad.AdRepository;
import com.auctioneer.repository.user.UserRepository;
import com.auctioneer.transformers.ad.AdTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdService {

    private final AdRepository adRepository;
    private final AdTransformer adTransformer;
    private final UserRepository userRepository;

    public AdDto get(Long adId) {
        Ad ad = adRepository.findById(adId).orElseThrow();

        return adTransformer.transform(ad);
    }

    public void create(AdDto adDto, Long userId) {
        Ad ad = adTransformer.transform(adDto);
        ad.setAuthorId(userId);

        adRepository.save(ad);
    }

    public List<AdDto> getMyAds(Long userId) {
        List<Ad> ads = adRepository.findAllByUserId(userId);


        return adTransformer.transform(ads);
    }

    //TODO: Refactor with correct way to update ad (without creating new one)
    public void edit(AdDto adDto) {
        Ad ad = adTransformer.transform(adDto);

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


