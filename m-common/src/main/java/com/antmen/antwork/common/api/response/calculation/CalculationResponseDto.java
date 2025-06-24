package com.antmen.antwork.common.api.response.calculation;

import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class CalculationResponseDto {
    private Long managerId;
    private Long reservationId;
    private LocalDate reservationDate;
    private Integer reservationAmount;
    private String categoryName;
}