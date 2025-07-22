package com.antmen.antwork.domain.payment.dto;






@Getter
@Builder
public class AdminSalesSummaryResponseDto {
    private Long totalSales;            // 총 매출
    private Long currentMonthSales;     // 이번달 매출
    private Long averageDailySales;     // 일간 평균 매출

    private Long totalProfit;           // 총 순이익
    private Long currentMonthProfit;    // 이번달 순이익
    private Long averageDailyProfit;    // 일간 평균 순이익

    private List<AdminDailySaleResponseDto> recentWeeklySalesProfit;
}