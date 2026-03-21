package com.auctioneer.transformers.shippingAddress;

import com.auctioneer.domain.entities.ShippingAddress;
import com.auctioneer.dtos.shippingAddress.ShippingAddressDto;
import org.springframework.stereotype.Component;

@Component
public class ShippingAddressTransformer {

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
