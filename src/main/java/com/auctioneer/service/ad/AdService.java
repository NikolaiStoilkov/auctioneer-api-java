package com.auctioneer.service.ad;

import com.auctioneer.domain.entities.Ad;
import com.auctioneer.dtos.ad.AdDto;
import com.auctioneer.repository.AdRepository;
import com.auctioneer.repository.UserRepository;
import com.auctioneer.transformers.ad.AdTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final AdTransformer adTransformer;

    public void create(AdDto adDto, Long userId) {
        // Check if user is authenticated


        Ad ad = adTransformer.transform(adDto);

        adRepository.save(ad);
    }

    public AdDto get(Long adId) {
        Ad ad = adRepository.findById(adId).orElseThrow();

        return adTransformer.transform(ad);
    }
}
