package com.antmen.antwork.domain.matching.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyMatchingResponseDto {
    private LocalDate date;
    private Long requestCount;
    private Long successCount;
    private BigDecimal matchingRate;
}
