package com.auctioneer.service.ad;

import com.auctioneer.domain.entities.Ad;
import com.auctioneer.domain.entities.LastBidder;
import com.auctioneer.domain.entities.User;
import com.auctioneer.dtos.ad.AdDto;
import com.auctioneer.dtos.ad.BidDto;
import com.auctioneer.repository.ad.AdRepository;
import com.auctioneer.repository.user.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdServiceTest {

    @Mock
    private AdRepository adRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdService adService;

    private Ad sampleAd;
    private AdDto sampleAdDto;
    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleAd = new Ad();
        sampleAd.setId(1L);
        sampleAd.setTitle("Test Ad");
        sampleAd.setDescription("A test ad description for testing");
        sampleAd.setBidStep(new BigDecimal("10.00"));
        sampleAd.setStartingBidPrice(new BigDecimal("100.00"));
        sampleAd.setCurrentBidPrice(new BigDecimal("100.00"));
        sampleAd.setAuthorId(1L);
        sampleAd.setLocation("Sofia");
        sampleAd.setLastBidders(new ArrayList<>());

        sampleAdDto = new AdDto();
        sampleAdDto.setTitle("Test Ad");
        sampleAdDto.setDescription("A test ad description for testing");
        sampleAdDto.setBidStep(new BigDecimal("10.00"));
        sampleAdDto.setStartingBidPrice(new BigDecimal("100.00"));
        sampleAdDto.setCurrentBidPrice(new BigDecimal("100.00"));
        sampleAdDto.setLocation("Sofia");

        sampleUser = User.builder()
                .id(1L)
                .username("john_doe")
                .email("john@example.com")
                .firstName("John")
                .lastName("Doe")
                .ucn("1234567890")
                .passwordHash("hashedpassword")
                .roles(List.of("USER"))
                .build();
    }

    @Test
    void getShouldReturnAdDtoWhenAdExists() {
        when(adRepository.findById(1L)).thenReturn(Optional.of(sampleAd));

        AdDto result = adService.get(1L);

        assertNotNull(result);
        assertEquals("Test Ad", result.getTitle());
        assertEquals("A test ad description for testing", result.getDescription());
        assertEquals(new BigDecimal("10.00"), result.getBidStep());
        assertEquals(new BigDecimal("100.00"), result.getStartingBidPrice());
        verify(adRepository).findById(1L);
    }

    @Test
    void getShouldThrowExceptionWhenAdDoesNotExist() {
        when(adRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> adService.get(99L));
        verify(adRepository).findById(99L);
    }

    @Test
    void createShouldSaveAd() {
        when(adRepository.save(any(Ad.class))).thenReturn(sampleAd);

        adService.create(sampleAdDto, 1L);

        verify(adRepository).save(any(Ad.class));
    }

    @Test
    void getMyAdsShouldReturnAdsForAuthor() {
        List<Ad> ads = List.of(sampleAd);
        when(adRepository.findAdByAuthorId(1L)).thenReturn(ads);

        List<AdDto> result = adService.getMyAds(1L);

        assertNotNull(result);
        verify(adRepository).findAdByAuthorId(1L);
    }

    @Test
    void editShouldSaveEditedAd() {
        when(adRepository.save(any(Ad.class))).thenReturn(sampleAd);

        adService.edit(sampleAdDto);

        verify(adRepository).save(any(Ad.class));
    }

    @Test
    void bidShouldUpdateCurrentBidPriceWhenBidIsHigher() {
        BidDto bidDto = new BidDto();
        bidDto.setAmount(new BigDecimal("150.00"));

        when(adRepository.findById(1L)).thenReturn(Optional.of(sampleAd));
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(adRepository.save(any(Ad.class))).thenReturn(sampleAd);

        adService.bid(1L, 1L, bidDto);

        assertEquals(new BigDecimal("150.00"), sampleAd.getCurrentBidPrice());
        assertEquals(1, sampleAd.getLastBidders().size());
        assertEquals("john_doe", sampleAd.getLastBidders().get(0).getUsername());
        verify(adRepository).save(sampleAd);
    }

    @Test
    void bidShouldThrowExceptionWhenBidIsLowerThanCurrentPrice() {
        BidDto bidDto = new BidDto();
        bidDto.setAmount(new BigDecimal("50.00"));

        when(adRepository.findById(1L)).thenReturn(Optional.of(sampleAd));
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> adService.bid(1L, 1L, bidDto)
        );

        assertEquals("Bid amount must be higher than current bid price", exception.getMessage());
        verify(adRepository, never()).save(any(Ad.class));
    }

    @Test
    void bidShouldThrowExceptionWhenBidEqualsCurrentPrice() {
        BidDto bidDto = new BidDto();
        bidDto.setAmount(new BigDecimal("100.00"));

        when(adRepository.findById(1L)).thenReturn(Optional.of(sampleAd));
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        assertThrows(IllegalArgumentException.class, () -> adService.bid(1L, 1L, bidDto));
        verify(adRepository, never()).save(any(Ad.class));
    }

    @Test
    void bidShouldThrowExceptionWhenAdNotFound() {
        BidDto bidDto = new BidDto();
        bidDto.setAmount(new BigDecimal("200.00"));

        when(adRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> adService.bid(99L, 1L, bidDto));
    }

    @Test
    void bidShouldThrowExceptionWhenUserNotFound() {
        BidDto bidDto = new BidDto();
        bidDto.setAmount(new BigDecimal("200.00"));

        when(adRepository.findById(1L)).thenReturn(Optional.of(sampleAd));
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> adService.bid(1L, 99L, bidDto));
    }
}
