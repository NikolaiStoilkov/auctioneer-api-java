package com.auctioneer.transformers.ad;

import com.auctioneer.domain.entities.Ad;
import com.auctioneer.dtos.ad.AdDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
        ad.setAuthor(adDto.getAuthor());
        ad.setLastBidder(adDto.getLastBidder());
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
        adDto.setAuthor(ad.getAuthor());
        adDto.setLastBidder(ad.getLastBidder());
        adDto.setLocation(ad.getLocation());

        return adDto;
    }
}
