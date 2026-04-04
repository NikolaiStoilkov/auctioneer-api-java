package com.auctioneer.repository.ad;

import com.auctioneer.domain.entities.Ad;
import com.auctioneer.domain.entities.Status;
import com.auctioneer.dtos.ad.AdFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {

    List<Ad> findAdByAuthorId(Long authorId);

    List<Ad> findAdByStatus(Status status);

    List<Ad> findAdByFilter(AdFilterDto filter);

    Page<Ad> findPage(PageRequest pageRequest);
}
