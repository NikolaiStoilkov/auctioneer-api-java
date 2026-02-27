package com.auctioneer.service.user;

import com.auctioneer.domain.entities.ShippingAddress;
import com.auctioneer.dtos.shippingAddress.ShippingAddressDto;
import com.auctioneer.repository.user.ShippingRepository;
import com.auctioneer.transformers.shippingAddress.ShippingAddressTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShippingService {
    private final ShippingRepository shippingRepository;
    private final ShippingAddressTransformer shippingAddressTransformer;

    public ShippingAddressDto get(Long userId) {
        ShippingAddress shippingAddress = shippingRepository.getById(userId);

        return shippingAddressTransformer.transform(shippingAddress);
    }

    public void save(ShippingAddressDto shippingAddressDto) {
        ShippingAddress shippingAddress = shippingAddressTransformer.transform(shippingAddressDto);

        shippingRepository.save(shippingAddress);
    }

    public void edit(ShippingAddressDto shippingAddressDto, Long id) {
        ShippingAddress shippingAddress = shippingAddressTransformer.transform(shippingAddressDto);

        ShippingAddress existingShippingAddress = shippingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shipping address not found with id: " + id));

        //TODO: What is correct way to update the existing shipping address with the new data?
        existingShippingAddress.setFirstName(shippingAddress.getFirstName());
        existingShippingAddress.setMiddleName(shippingAddress.getMiddleName());
        existingShippingAddress.setLastName(shippingAddress.getLastName());
        existingShippingAddress.setPhoneNumber(shippingAddress.getPhoneNumber());
        existingShippingAddress.setCountry(shippingAddress.getCountry());
        existingShippingAddress.setCity(shippingAddress.getCity());
        existingShippingAddress.setStreet(shippingAddress.getStreet());
        existingShippingAddress.setStreetNumber(shippingAddress.getStreetNumber());
        existingShippingAddress.setPostalCode(shippingAddress.getPostalCode());

        shippingRepository.save(existingShippingAddress);
    }
}
