package com.antmen.antwork.domain.calculation.dto;







@Getter
@Builder
public class AdminMonthCalculationResponseDto {
    private Long calculationId;
    private Long managerId;
    private String managerName;
    private LocalDate startDate;
    private LocalDate endDate;
    private int amount;
    private LocalDateTime requestedAt;
}