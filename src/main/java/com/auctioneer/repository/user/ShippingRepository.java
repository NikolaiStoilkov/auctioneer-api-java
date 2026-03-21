package com.auctioneer.repository.user;

import com.auctioneer.domain.entities.ShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingRepository extends JpaRepository<ShippingAddress, Long> {
    ShippingAddress getById(Long userId);
}
