package com.auctioneer.transformers.ad;

import com.auctioneer.domain.entities.Ad;
import com.auctioneer.dtos.ad.AdRequestDto;
import com.auctioneer.dtos.ad.AdResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Maps between the {@link Ad} entity and its request/response DTOs.
 */
@Component
@RequiredArgsConstructor
public class AdTransformer {

    /**
     * Builds a new {@link Ad} entity from a client payload. Id, author and
     * bid state are intentionally not mapped — they are server-controlled.
     *
     * @param adDto the client payload
     * @return the new entity
     */
    public Ad transform(AdRequestDto adDto) {
        Ad ad = new Ad();

        ad.setTitle(adDto.getTitle());
        ad.setImage(adDto.getImage());
        ad.setDescription(adDto.getDescription());
        ad.setBidStep(adDto.getBidStep());
        ad.setStartingBidPrice(adDto.getStartingBidPrice());
        ad.setLocation(adDto.getLocation());
        ad.setImages(adDto.getImages());
        ad.setStatus(adDto.getStatus());
        ad.setIsActive(adDto.getIsActive());
        ad.setStartingDate(adDto.getStartingDate());
        ad.setEndDate(adDto.getEndDate());

        return ad;
    }

    /**
     * Builds a response DTO from an {@link Ad} entity.
     *
     * @param ad the entity
     * @return the response DTO
     */
    public AdResponseDto transform(Ad ad) {
        AdResponseDto adDto = new AdResponseDto();

        adDto.setId(ad.getId());
        adDto.setTitle(ad.getTitle());
        adDto.setImage(ad.getImage());
        adDto.setDescription(ad.getDescription());
        adDto.setBidStep(ad.getBidStep());
        adDto.setStartingBidPrice(ad.getStartingBidPrice());
        adDto.setCurrentBidPrice(ad.getCurrentBidPrice());
        adDto.setAuthorId(ad.getAuthorId());
        adDto.setLastBidders(ad.getLastBidders());
        adDto.setLocation(ad.getLocation());
        adDto.setImages(ad.getImages());
        adDto.setStatus(ad.getStatus());
        adDto.setIsActive(ad.getIsActive());
        adDto.setStartingDate(ad.getStartingDate());
        adDto.setEndDate(ad.getEndDate());

        return adDto;
    }

    /**
     * Maps a list of entities to response DTOs.
     *
     * @param ads the entities
     * @return the response DTOs
     */
    public List<AdResponseDto> transform(List<Ad> ads) {
        return ads.stream().map(this::transform).toList();
    }
}
