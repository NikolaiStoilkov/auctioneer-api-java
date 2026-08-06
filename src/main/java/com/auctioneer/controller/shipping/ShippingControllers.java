package com.auctioneer.controller.shipping;

import com.auctioneer.dtos.shippingAddress.ShippingAddressDto;
import com.auctioneer.dtos.user.UserPrincipal;
import com.auctioneer.service.user.ShippingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shipping")
@RequiredArgsConstructor
public class ShippingControllers {
    private final ShippingService shippingService;

    /**
     * Returns the shipping address of a user.
     *
     * @param userId    the id of the user
     * @param principal the authenticated user
     * @return the user's shipping address
     */
    @GetMapping("/{userId}")
    public ShippingAddressDto getShippingInfoByUserId(@PathVariable  Long userId,@AuthenticationPrincipal UserPrincipal principal) {
        return shippingService.get(userId);
    }

    /**
     * Saves a new shipping address.
     *
     * @param shippingAddressDto the shipping address to save
     * @param principal          the authenticated user
     */
    @PostMapping("/save")
    public void saveShippingInfo(@Valid @RequestBody ShippingAddressDto shippingAddressDto,@AuthenticationPrincipal UserPrincipal principal) {
        shippingService.save(shippingAddressDto);
    }

    /**
     * Updates an existing shipping address.
     *
     * @param shippingAddressDto the new shipping address data
     * @param id                 the id of the shipping address to update
     * @param principal          the authenticated user
     */
    @PatchMapping("/edit/{id}")
    public void edit(@Valid @RequestBody ShippingAddressDto shippingAddressDto, @PathVariable Long id,@AuthenticationPrincipal UserPrincipal principal) {
        shippingService.edit(shippingAddressDto,id);
    }
}
