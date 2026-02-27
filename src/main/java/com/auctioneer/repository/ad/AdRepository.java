package com.auctioneer.repository.ad;

import com.auctioneer.domain.entities.Ad;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {

    @Query("SELECT a FROM Ad a WHERE a.authorId = :userId")
    List<Ad> findAllByUserId(Long userId);
}
