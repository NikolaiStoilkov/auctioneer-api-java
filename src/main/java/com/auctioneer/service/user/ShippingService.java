package com.auctioneer.service.user;

import com.auctioneer.domain.entities.ShippingAddress;
import com.auctioneer.dtos.shippingAddress.ShippingAddressDto;
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
                .orElseThrow(() -> new RuntimeException("Shipping address not found for user id: " + userId));

        ShippingAddressDto shippingAddressDto = new ShippingAddressDto();

        BeanUtils.copyProperties(shippingAddress, shippingAddressDto);

        return shippingAddressDto;
    }

    public void save(ShippingAddressDto shippingAddressDto) {
        ShippingAddress shippingAddress = new ShippingAddress();

        BeanUtils.copyProperties(shippingAddressDto, shippingAddress);

        shippingRepository.save(shippingAddress);
        String message = String.format("📦 Shipping address saved for %s %s", shippingAddressDto.getFirstName(), shippingAddressDto.getLastName());

        discordService.sendUserNotification(message);
    }

    public void edit(ShippingAddressDto shippingAddressDto, Long id) {
        ShippingAddress existingShippingAddress = shippingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shipping address not found with id: " + id));

        BeanUtils.copyProperties(shippingAddressDto, existingShippingAddress, "id"); //

        shippingRepository.save(existingShippingAddress);
        String message = String.format("✏️ Shipping address updated (ID: %d) for %s %s", id, shippingAddressDto.getFirstName(), shippingAddressDto.getLastName());

        discordService.sendUserNotification(message);
    }
}
