package com.antmen.antwork.admin.api;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class AdminDailySaleResponseDto {
    private LocalDate dailyDate;
    private Long dailySales;
    private Long dailyFee;
    private Long dailyProfit;
}