package com.auctioneer.transformers.ad;

import com.auctioneer.domain.entities.Ad;
import com.auctioneer.dtos.ad.AdDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AdTransformer {

    public Ad transform(AdDto adDto) {
        Ad ad = new Ad();

        ad.setTitle(adDto.getTitle());
        ad.setImage(adDto.getImage());
        ad.setDescription(adDto.getDescription());
        ad.setBidStep(adDto.getBidStep());
        ad.setStartingBidPrice(adDto.getStartingBidPrice());
        ad.setCurrentBidPrice(adDto.getCurrentBidPrice());
        ad.setAuthorId(adDto.getAuthorId());
        ad.setLastBidders(adDto.getLastBidders());
        ad.setLocation(adDto.getLocation());

        return ad;
    }

    public AdDto transform(Ad ad) {
        AdDto adDto = new AdDto();

        adDto.setTitle(ad.getTitle());
        adDto.setImage(ad.getImage());
        adDto.setDescription(ad.getDescription());
        adDto.setBidStep(ad.getBidStep());
        adDto.setStartingBidPrice(ad.getStartingBidPrice());
        adDto.setCurrentBidPrice(ad.getCurrentBidPrice());
        adDto.setAuthorId(ad.getAuthorId());
        adDto.setLastBidders(ad.getLastBidders());
        adDto.setLocation(ad.getLocation());

        return adDto;
    }

    public List<AdDto> transform(List<Ad> ads) {
        return ads.stream().map(this::transform).toList();
    }


}
