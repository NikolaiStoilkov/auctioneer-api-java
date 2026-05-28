package com.auctioneer.repository.wallet;

import com.auctioneer.domain.entities.CreditTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditTransactionRepository extends JpaRepository<CreditTransaction, Long> {
    Page<CreditTransaction> findAllByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    boolean existsByStripeSessionId(String stripeSessionId);
}

