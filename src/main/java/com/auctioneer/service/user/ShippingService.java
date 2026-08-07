package com.auctioneer.service.user;

import com.auctioneer.domain.entities.ShippingAddress;
import com.auctioneer.dtos.shippingAddress.ShippingAddressDto;
import com.auctioneer.exceptions.ShippingAddressNotFoundException;
import com.auctioneer.repository.user.ShippingRepository;
import com.auctioneer.service.discordNotifications.DiscordService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShippingService {
    private final ShippingRepository shippingRepository;
    private final DiscordService discordService;

    public ShippingAddressDto get(Long userId) {
        ShippingAddress shippingAddress = shippingRepository.findById(userId)
                .orElseThrow(() -> new ShippingAddressNotFoundException(userId));

        ShippingAddressDto shippingAddressDto = new ShippingAddressDto();

        BeanUtils.copyProperties(shippingAddress, shippingAddressDto);

        return shippingAddressDto;
    }

    public void save(ShippingAddressDto shippingAddressDto) {
        ShippingAddress shippingAddress = new ShippingAddress();

        BeanUtils.copyProperties(shippingAddressDto, shippingAddress);

        shippingRepository.save(shippingAddress);
        discordService.sendUserNotification("📦 Shipping address saved for " + shippingAddressDto.getFirstName() + " " + shippingAddressDto.getLastName());
    }

    public void edit(ShippingAddressDto shippingAddressDto, Long id) {
        ShippingAddress existingShippingAddress = shippingRepository.findById(id)
                .orElseThrow(() -> new ShippingAddressNotFoundException(id));

        BeanUtils.copyProperties(shippingAddressDto, existingShippingAddress, "id"); //

        shippingRepository.save(existingShippingAddress);
        discordService.sendUserNotification("✏️ Shipping address updated (ID: " + id + ") for " + shippingAddressDto.getFirstName() + " " + shippingAddressDto.getLastName());
    }
}
