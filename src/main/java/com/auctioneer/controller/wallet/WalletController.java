package com.auctioneer.controller.wallet;

import com.auctioneer.dtos.user.UserPrincipal;
import com.auctioneer.dtos.wallet.BalanceDto;
import com.auctioneer.dtos.wallet.CheckoutRequestDto;
import com.auctioneer.dtos.wallet.CreditTransactionDto;
import com.auctioneer.service.stripe.StripeService;
import com.auctioneer.service.wallet.WalletService;
import com.stripe.exception.StripeException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;
    private final StripeService stripeService;

    @GetMapping("/balance")
    public BalanceDto getBalance(@AuthenticationPrincipal UserPrincipal principal) {
        return walletService.getBalance(principal.getId());
    }

    @PostMapping("/create-payment-intent")
    public Map<String, String> createPaymentIntent(
            @Valid @RequestBody CheckoutRequestDto request,
            @AuthenticationPrincipal UserPrincipal principal) throws StripeException {
        String clientSecret = stripeService.createPaymentIntent(principal.getId(), request.getAmount());
        return Map.of("clientSecret", clientSecret);
    }

    @PostMapping("/confirm-credits")
    public BalanceDto confirmCredits(
            @Valid @RequestBody CheckoutRequestDto request,
            @AuthenticationPrincipal UserPrincipal principal) {
        return walletService.addCredits(principal.getId(), request.getAmount());
    }

    @PostMapping("/add-credits")
    public BalanceDto addCredits(
            @Valid @RequestBody CheckoutRequestDto request,
            @AuthenticationPrincipal UserPrincipal principal) {
        return walletService.addCredits(principal.getId(), request.getAmount());
    }

    @GetMapping("/transactions")
    public Page<CreditTransactionDto> getTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserPrincipal principal) {
        return walletService.getTransactions(principal.getId(), page, size);
    }
}


