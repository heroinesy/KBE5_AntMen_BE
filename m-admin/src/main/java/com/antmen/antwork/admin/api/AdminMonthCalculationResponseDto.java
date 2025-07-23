package com.antmen.antwork.admin.api;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

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