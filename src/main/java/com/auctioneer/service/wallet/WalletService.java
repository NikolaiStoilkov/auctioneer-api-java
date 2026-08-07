package com.auctioneer.service.wallet;

import com.auctioneer.domain.entities.CreditTransaction;
import com.auctioneer.domain.entities.User;
import com.auctioneer.dtos.wallet.BalanceDto;
import com.auctioneer.dtos.wallet.CreditTransactionDto;
import com.auctioneer.exceptions.InsufficientBalanceException;
import com.auctioneer.exceptions.UserNotFoundException;
import com.auctioneer.repository.user.UserRepository;
import com.auctioneer.repository.wallet.CreditTransactionRepository;
import com.auctioneer.service.discordNotifications.DiscordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final UserRepository userRepository;
    private final CreditTransactionRepository creditTransactionRepository;
    private final DiscordService discordService;

    public BalanceDto getBalance(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return new BalanceDto(user.getBalance(), user.getCredits());
    }

    @Transactional
    public BalanceDto addCredits(Long userId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ONE) < 0) {
            throw new IllegalArgumentException("Minimum amount is 1.00");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.setCredits(user.getCredits().add(amount));
        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);

        creditTransactionRepository.save(
                CreditTransaction.builder()
                        .userId(userId)
                        .amount(amount)
                        .type(CreditTransaction.TransactionType.PURCHASE)
                        .description("Credits purchased: " + amount)
                        .build()
        );

        discordService.sendWalletNotification("💳 User " + userId + " added **" + amount + "** credits");
        return new BalanceDto(user.getBalance(), user.getCredits());
    }

    @Transactional
    public void debit(Long userId, BigDecimal amount, String description) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (user.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException();
        }

        user.setBalance(user.getBalance().subtract(amount));
        user.setCredits(user.getCredits().subtract(amount));
        userRepository.save(user);

        creditTransactionRepository.save(CreditTransaction.builder()
                .userId(userId).amount(amount)
                .type(CreditTransaction.TransactionType.DEBIT)
                .description(description).build());

        discordService.sendWalletNotification("💸 User " + userId + " debited **" + amount + "** — " + description);
    }

    @Transactional
    public void refund(Long userId, BigDecimal amount, String description) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.setBalance(user.getBalance().add(amount));
        user.setCredits(user.getCredits().add(amount));
        userRepository.save(user);

        creditTransactionRepository.save(CreditTransaction.builder()
                .userId(userId).amount(amount)
                .type(CreditTransaction.TransactionType.REFUND)
                .description(description).build());

        discordService.sendWalletNotification("♻️ User " + userId + " refunded **" + amount + "** — " + description);
    }

    public Page<CreditTransactionDto> getTransactions(Long userId, int page, int size) {
        return creditTransactionRepository
                .findAllByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(page, size))
                .map(t -> CreditTransactionDto.builder()
                        .id(t.getId())
                        .amount(t.getAmount())
                        .type(t.getType().name())
                        .description(t.getDescription())
                        .createdAt(t.getCreatedAt())
                        .build());
    }
}

