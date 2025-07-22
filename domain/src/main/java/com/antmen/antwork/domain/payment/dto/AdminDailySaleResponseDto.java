package com.antmen.antwork.domain.payment.dto;






@Getter
@Builder
public class AdminDailySaleResponseDto {
    private LocalDate dailyDate;
    private Long dailySales;
    private Long dailyFee;
    private Long dailyProfit;
}