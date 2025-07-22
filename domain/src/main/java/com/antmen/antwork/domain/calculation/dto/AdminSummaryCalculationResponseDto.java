package com.antmen.antwork.domain.calculation.dto;






@Getter
@Builder
public class AdminSummaryCalculationResponseDto {
    private Long totalAmount;               // 총 정산 금액
    private Long currentMonthAmount;        // 이번 달 정산 금액
    private Long currentWeekAmount;         // 이번 주 정산 금액
    private Integer calculationCount;       // 정산 완료 건수

    private List<AdminMonthCalculationResponseDto> recentMonthCalculations; // 최근 1개월 정산 내역
}