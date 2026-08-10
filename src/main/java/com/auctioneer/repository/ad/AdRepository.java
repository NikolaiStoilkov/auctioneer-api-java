package com.auctioneer.repository.ad;

import com.auctioneer.domain.entities.Ad;
import com.auctioneer.domain.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Data access for {@link Ad}. Concurrency is handled by the {@code @Version}
 * column on the entity (optimistic locking), so no pessimistic lock query is
 * needed.
 */
@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {

    List<Ad> findAdByAuthorId(Long authorId);

    List<Ad> findAdByStatus(Status status);

    List<Ad> findAdByIsActiveTrueAndEndDateBefore(LocalDate date);
}
