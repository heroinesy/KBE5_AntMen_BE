package com.antmen.antwork.admin.api;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminSalesSummaryResponseDto {
    private Long totalSales;            // 총 매출
    private Long currentMonthSales;     // 최근 한달 매출
    private Long averageDailySales;     // 일간 평균 매출
}
