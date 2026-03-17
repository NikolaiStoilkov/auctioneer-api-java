package com.auctioneer.service.user;

import com.auctioneer.domain.entities.ShippingAddress;
import com.auctioneer.dtos.shippingAddress.ShippingAddressDto;
import com.auctioneer.repository.user.ShippingRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShippingService {
    private final ShippingRepository shippingRepository;

    public ShippingAddressDto get(Long userId) {
        // Use BeanUtils to copy properties from ShippingAddress to ShippingAddressDto
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
    }

    public void edit(ShippingAddressDto shippingAddressDto, Long id) {
        ShippingAddress existingShippingAddress = shippingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shipping address not found with id: " + id));

        BeanUtils.copyProperties(shippingAddressDto, existingShippingAddress, "id"); //

        shippingRepository.save(existingShippingAddress);
    }
}
