package com.auctioneer.service;

import com.auctioneer.domain.entities.Ad;
import com.auctioneer.domain.entities.User;
import com.auctioneer.dtos.AdDto;
import com.auctioneer.repository.AdRepository;
import com.auctioneer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;

    public void create(AdDto adDto, Long userId) {
        Ad ad = new Ad();
        User user = userRepository.findById(userId).orElseThrow();
        ad.setAuthor(user);
        // TODO transform from dto

        adRepository.save(ad);
    }
}
