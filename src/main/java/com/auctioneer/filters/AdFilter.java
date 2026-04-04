package com.auctioneer.filters;

import lombok.Getter;

import java.time.LocalDate;

@Getter
private static class AdFilter {
    private Boolean active;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private int page;
    private int size;
}
