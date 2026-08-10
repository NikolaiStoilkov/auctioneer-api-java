package com.auctioneer.repository.wallet;

import com.auctioneer.domain.entities.CreditTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Data access for {@link CreditTransaction} ledger entries.
 */
@Repository
public interface CreditTransactionRepository extends JpaRepository<CreditTransaction, Long> {
    /**
     * Returns a user's transactions, newest first.
     *
     * @param userId   the user id
     * @param pageable the paging request
     * @return the page of transactions
     */
    Page<CreditTransaction> findAllByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    /**
     * Checks whether a transaction with the given Stripe session id exists
     * (used for idempotent webhook handling).
     *
     * @param stripeSessionId the Stripe checkout session id
     * @return whether a matching transaction exists
     */
    boolean existsByStripeSessionId(String stripeSessionId);
}

