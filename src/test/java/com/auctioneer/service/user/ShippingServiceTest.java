package com.auctioneer.service.user;

import com.auctioneer.domain.entities.ShippingAddress;
import com.auctioneer.dtos.shippingAddress.ShippingAddressDto;
import com.auctioneer.repository.user.ShippingRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShippingServiceTest {

    @Mock
    private ShippingRepository shippingRepository;

    @InjectMocks
    private ShippingService shippingService;

    private ShippingAddress sampleShippingAddress;
    private ShippingAddressDto sampleShippingAddressDto;

    @BeforeEach
    void setUp() {
        sampleShippingAddress = new ShippingAddress();
        sampleShippingAddress.setId(1L);
        sampleShippingAddress.setFirstName("John");
        sampleShippingAddress.setMiddleName("Michael");
        sampleShippingAddress.setLastName("Doe");
        sampleShippingAddress.setPhoneNumber("+359888111222");
        sampleShippingAddress.setCountry("Bulgaria");
        sampleShippingAddress.setCity("Sofia");
        sampleShippingAddress.setStreet("Vitosha Blvd");
        sampleShippingAddress.setStreetNumber("101");
        sampleShippingAddress.setPostalCode("1000");

        sampleShippingAddressDto = new ShippingAddressDto();
        sampleShippingAddressDto.setFirstName("John");
        sampleShippingAddressDto.setMiddleName("Michael");
        sampleShippingAddressDto.setLastName("Doe");
        sampleShippingAddressDto.setPhoneNumber("+359888111222");
        sampleShippingAddressDto.setCountry("Bulgaria");
        sampleShippingAddressDto.setCity("Sofia");
        sampleShippingAddressDto.setStreet("Vitosha Blvd");
        sampleShippingAddressDto.setStreetNumber("101");
        sampleShippingAddressDto.setPostalCode("1000");
    }

    @Test
    void get_shouldReturnShippingAddressDto_whenFound() {
        when(shippingRepository.findById(1L)).thenReturn(Optional.of(sampleShippingAddress));

        ShippingAddressDto result = shippingService.get(1L);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Michael", result.getMiddleName());
        assertEquals("Doe", result.getLastName());
        assertEquals("+359888111222", result.getPhoneNumber());
        assertEquals("Bulgaria", result.getCountry());
        assertEquals("Sofia", result.getCity());
        assertEquals("Vitosha Blvd", result.getStreet());
        assertEquals("101", result.getStreetNumber());
        assertEquals("1000", result.getPostalCode());
        verify(shippingRepository).findById(1L);
    }

    @Test
    void get_shouldThrowException_whenNotFound() {
        when(shippingRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> shippingService.get(99L)
        );

        assertEquals("Shipping address not found for user id: 99", exception.getMessage());
        verify(shippingRepository).findById(99L);
    }

    @Test
    void save_shouldPersistShippingAddress() {
        when(shippingRepository.save(any(ShippingAddress.class))).thenReturn(sampleShippingAddress);

        shippingService.save(sampleShippingAddressDto);

        verify(shippingRepository).save(any(ShippingAddress.class));
    }

    @Test
    void edit_shouldUpdateExistingShippingAddress() {
        when(shippingRepository.findById(1L)).thenReturn(Optional.of(sampleShippingAddress));
        when(shippingRepository.save(any(ShippingAddress.class))).thenReturn(sampleShippingAddress);

        ShippingAddressDto updatedDto = new ShippingAddressDto();
        updatedDto.setFirstName("Jane");
        updatedDto.setMiddleName("Marie");
        updatedDto.setLastName("Smith");
        updatedDto.setPhoneNumber("+359888222333");
        updatedDto.setCountry("Bulgaria");
        updatedDto.setCity("Plovdiv");
        updatedDto.setStreet("Main St");
        updatedDto.setStreetNumber("55");
        updatedDto.setPostalCode("4000");

        shippingService.edit(updatedDto, 1L);

        verify(shippingRepository).findById(1L);
        verify(shippingRepository).save(any(ShippingAddress.class));
        // Verify that the id was preserved (not overwritten)
        assertEquals(1L, sampleShippingAddress.getId());
    }

    @Test
    void edit_shouldThrowException_whenNotFound() {
        when(shippingRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> shippingService.edit(sampleShippingAddressDto, 99L)
        );

        assertEquals("Shipping address not found with id: 99", exception.getMessage());
        verify(shippingRepository).findById(99L);
        verify(shippingRepository, never()).save(any(ShippingAddress.class));
    }

    @Test
    void edit_shouldPreserveId_afterCopyProperties() {
        when(shippingRepository.findById(1L)).thenReturn(Optional.of(sampleShippingAddress));
        when(shippingRepository.save(any(ShippingAddress.class))).thenAnswer(invocation -> invocation.getArgument(0));

        shippingService.edit(sampleShippingAddressDto, 1L);

        verify(shippingRepository).save(argThat(address ->
                address.getId().equals(1L) &&
                address.getFirstName().equals("John") &&
                address.getLastName().equals("Doe")
        ));
    }
}

