package com.antmen.antwork.common.api.response.calculation;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class CalculationResponseDto {
    private Long calculationId;
    private Long managerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer amount;
    private Long reservationId;
    private LocalDate reservationDate;
    private Integer reservationAmount;
    private String categoryName;
    private LocalDateTime requsetedAt;
}