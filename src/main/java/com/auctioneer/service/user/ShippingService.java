package com.auctioneer.service.user;

import com.auctioneer.domain.entities.ShippingAddress;
import com.auctioneer.dtos.shippingAddress.ShippingAddressDto;
import com.auctioneer.exceptions.ShippingAddressNotFoundException;
import com.auctioneer.repository.user.ShippingRepository;
import com.auctioneer.service.discordNotifications.DiscordService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * Manages user {@link ShippingAddress} records.
 */
@Service
@RequiredArgsConstructor
public class ShippingService {
    private final ShippingRepository shippingRepository;
    private final DiscordService discordService;

    /**
     * Returns the shipping address for a user.
     *
     * @param userId the id of the user
     * @return the shipping address
     * @throws ShippingAddressNotFoundException if none exists
     */
    public ShippingAddressDto get(Long userId) {
        ShippingAddress shippingAddress = shippingRepository.findById(userId)
                .orElseThrow(() -> new ShippingAddressNotFoundException(userId));

        ShippingAddressDto shippingAddressDto = new ShippingAddressDto();

        BeanUtils.copyProperties(shippingAddress, shippingAddressDto);

        return shippingAddressDto;
    }

    /**
     * Saves a new shipping address.
     *
     * @param shippingAddressDto the shipping address to save
     */
    public void save(ShippingAddressDto shippingAddressDto) {
        ShippingAddress shippingAddress = new ShippingAddress();

        BeanUtils.copyProperties(shippingAddressDto, shippingAddress);

        shippingRepository.save(shippingAddress);
        discordService.sendUserNotification("📦 Shipping address saved for " + shippingAddressDto.getFirstName() + " " + shippingAddressDto.getLastName());
    }

    /**
     * Updates an existing shipping address (the id is never overwritten).
     *
     * @param shippingAddressDto the new address data
     * @param id                 the id of the address to update
     * @throws ShippingAddressNotFoundException if none exists
     */
    public void edit(ShippingAddressDto shippingAddressDto, Long id) {
        ShippingAddress existingShippingAddress = shippingRepository.findById(id)
                .orElseThrow(() -> new ShippingAddressNotFoundException(id));

        BeanUtils.copyProperties(shippingAddressDto, existingShippingAddress, "id"); //

        shippingRepository.save(existingShippingAddress);
        discordService.sendUserNotification("✏️ Shipping address updated (ID: " + id + ") for " + shippingAddressDto.getFirstName() + " " + shippingAddressDto.getLastName());
    }
}
