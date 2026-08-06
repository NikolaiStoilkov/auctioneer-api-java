package com.auctioneer.dtos.ad;

import com.auctioneer.domain.entities.LastBidder;
import com.auctioneer.domain.entities.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class AdDto {
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    private String image;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters")
    private String description;

    @NotNull(message = "Bid step is required")
    @Positive(message = "Bid step must be positive")
    private BigDecimal bidStep;

    @NotNull(message = "Starting bid price is required")
    @Positive(message = "Starting bid price must be positive")
    private BigDecimal startingBidPrice;

    @PositiveOrZero(message = "Current bid price must be zero or positive")
    private BigDecimal currentBidPrice;

    private Long authorId;

    private List<LastBidder> lastBidders;

    @NotBlank(message = "Location is required")
    private String location;

    private List<String> images;

    private Status status;

    private Boolean isActive;

    private LocalDate startingDate;

    private LocalDate endDate;
}
