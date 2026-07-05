package com.auctioneer.controller.stripe;

import com.auctioneer.dtos.stripe.PaymentMethodResponseDto;
import com.auctioneer.dtos.user.UserPrincipal;
import com.auctioneer.service.stripe.StripeService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stripe")
@RequiredArgsConstructor
public class StripeController {
    private final StripeService stripeService;

    @GetMapping("/config")
    public Map<String, String> getConfig() {
        return Map.of("publishableKey", stripeService.getPublishableKey());
    }

    @PostMapping("/setup-intent")
    public Map<String, String> createSetupIntent(
            @AuthenticationPrincipal UserPrincipal principal) throws StripeException {
        String clientSecret = stripeService.createSetupIntent(principal.getId());
        return Map.of("clientSecret", clientSecret);
    }

    @GetMapping("/payment-methods")
    public List<PaymentMethodResponseDto> listSavedCards(
            @AuthenticationPrincipal UserPrincipal principal) throws StripeException {
        return stripeService.listSavedCards(principal.getId());
    }

    @PostMapping("/save-customer-payment-method")
    public PaymentMethodResponseDto createCustomerAccount(
            @AuthenticationPrincipal UserPrincipal principal) throws StripeException {
        return PaymentMethodResponseDto.from(
                stripeService.createCustomerAccount(principal.getId()));
    }
}
