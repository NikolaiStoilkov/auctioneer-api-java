package com.auctioneer.dtos.ad;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AdFilterDto {
    private Boolean active;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private int page = 1;
    private int size = 10;
}
