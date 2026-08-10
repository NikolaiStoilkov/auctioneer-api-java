package com.auctioneer.transformers.shippingAddress;

import com.auctioneer.domain.entities.ShippingAddress;
import com.auctioneer.dtos.shippingAddress.ShippingAddressDto;
import org.springframework.stereotype.Component;

/**
 * Maps between the {@link ShippingAddress} entity and its DTO.
 */
@Component
public class ShippingAddressTransformer {

    /**
     * Maps a {@link ShippingAddressDto} to an entity.
     *
     * @param shippingAddressDto the source DTO
     * @return the mapped entity
     */
    public ShippingAddress transform (ShippingAddressDto shippingAddressDto) {
        ShippingAddress shippingAddress = new ShippingAddress();

        shippingAddress.setFirstName(shippingAddressDto.getFirstName());
        shippingAddress.setMiddleName(shippingAddressDto.getMiddleName());
        shippingAddress.setLastName(shippingAddressDto.getLastName());
        shippingAddress.setPhoneNumber(shippingAddressDto.getPhoneNumber());
        shippingAddress.setCountry(shippingAddressDto.getCountry());
        shippingAddress.setCity(shippingAddressDto.getCity());
        shippingAddress.setStreet(shippingAddressDto.getStreet());
        shippingAddress.setStreetNumber(shippingAddressDto.getStreetNumber());
        shippingAddress.setPostalCode(shippingAddressDto.getPostalCode());

        return shippingAddress;
    }

    /**
     * Maps a {@link ShippingAddress} entity to its DTO.
     *
     * @param shippingAddress the source entity
     * @return the mapped DTO
     */
    public ShippingAddressDto transform (ShippingAddress shippingAddress) {
        ShippingAddressDto dto = new ShippingAddressDto();

        dto.setFirstName(shippingAddress.getFirstName());
        dto.setMiddleName(shippingAddress.getMiddleName());
        dto.setLastName(shippingAddress.getLastName());
        dto.setPhoneNumber(shippingAddress.getPhoneNumber());
        dto.setCountry(shippingAddress.getCountry());
        dto.setCity(shippingAddress.getCity());
        dto.setStreet(shippingAddress.getStreet());
        dto.setStreetNumber(shippingAddress.getStreetNumber());
        dto.setPostalCode(shippingAddress.getPostalCode());

        return dto;
    }
}
