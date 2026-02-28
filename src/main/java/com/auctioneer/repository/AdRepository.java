package com.auctioneer.repository;

import com.auctioneer.domain.entities.Ad;
import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface AdRepository extends CrudRepository<Ad, Long> { }
