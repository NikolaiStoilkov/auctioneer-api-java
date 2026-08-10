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

    /**
     * Returns the Stripe publishable key for the frontend.
     *
     * @return a map containing the publishable key
     */
    @GetMapping("/config")
    public Map<String, String> getConfig() {
        return Map.of("publishableKey", stripeService.getPublishableKey());
    }

    /**
     * Creates a Stripe setup intent for saving a payment method.
     *
     * @param principal the authenticated user
     * @return a map containing the Stripe client secret
     * @throws StripeException if the Stripe API call fails
     */
    @PostMapping("/setup-intent")
    public Map<String, String> createSetupIntent(
            @AuthenticationPrincipal UserPrincipal principal) throws StripeException {
        String clientSecret = stripeService.createSetupIntent(principal.getId());
        return Map.of("clientSecret", clientSecret);
    }

    /**
     * Returns the authenticated user's saved cards.
     *
     * @param principal the authenticated user
     * @return the saved payment methods
     * @throws StripeException if the Stripe API call fails
     */
    @GetMapping("/payment-methods")
    public List<PaymentMethodResponseDto> listSavedCards(
            @AuthenticationPrincipal UserPrincipal principal) throws StripeException {
        return stripeService.listSavedCards(principal.getId());
    }

    /**
     * Creates a Stripe customer account for the authenticated user and returns its payment method.
     *
     * @param principal the authenticated user
     * @return the customer's payment method
     * @throws StripeException if the Stripe API call fails
     */
    @PostMapping("/save-customer-payment-method")
    public PaymentMethodResponseDto createCustomerAccount(
            @AuthenticationPrincipal UserPrincipal principal) throws StripeException {
        return PaymentMethodResponseDto.from(
                stripeService.createCustomerAccount(principal.getId()));
    }
}
