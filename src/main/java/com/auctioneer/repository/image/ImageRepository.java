package com.auctioneer.repository.image;

import com.auctioneer.domain.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Data access for {@link Image} binary records.
 */
@Repository
public interface ImageRepository extends JpaRepository<Image, String> {
}
