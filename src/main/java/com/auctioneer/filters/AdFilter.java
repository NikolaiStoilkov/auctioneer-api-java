package com.auctioneer.filters;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AdFilter {
    private Boolean active;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private int page;
    private int size;
}
