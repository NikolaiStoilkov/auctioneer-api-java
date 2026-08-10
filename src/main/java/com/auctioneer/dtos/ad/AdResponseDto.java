package com.auctioneer.dtos.ad;

import com.auctioneer.domain.entities.LastBidder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * Ad representation returned to clients: the shared base fields plus the
 * server-assigned id, author and current bid state.
 */
@Getter
@Setter
public class AdResponseDto extends AdBaseDto {

    private Long id;

    private Long authorId;

    private BigDecimal currentBidPrice;

    private List<LastBidder> lastBidders;
}
