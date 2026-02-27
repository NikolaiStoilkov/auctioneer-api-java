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

    @GetMapping("/{userId}")
    public ShippingAddressDto getShippingInfoByUserId(@PathVariable  Long userId,@AuthenticationPrincipal UserPrincipal principal) {
        return shippingService.get(userId);
    }

    @PostMapping("/save")
    public void saveShippingInfo(@Valid @RequestBody ShippingAddressDto shippingAddressDto,@AuthenticationPrincipal UserPrincipal principal) {
        shippingService.save(shippingAddressDto);
    }

    @PatchMapping("/edit/{id}")
    public void edit(@Valid @RequestBody ShippingAddressDto shippingAddressDto, @PathVariable Long id,@AuthenticationPrincipal UserPrincipal principal) {
        shippingService.edit(shippingAddressDto,id);
    }
}
