package com.auctioneer.repository.ad;

import com.auctioneer.domain.entities.Ad;
import com.auctioneer.domain.entities.Status;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {

    List<Ad> findAdByAuthorId(Long authorId);

    List<Ad> findAdByStatus(Status status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Ad a WHERE a.id = :id")
    Optional<Ad> findByIdForUpdate(Long id);
}
