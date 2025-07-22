package com.antmen.antwork.domain.matching.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchingSummaryResponseDto {
    private BigDecimal matchingRating;
    private Long totalMatchingCount;
    private Long successCount;
    private Long failCount;

    private BigDecimal customerRefuseRate;
    private BigDecimal managerRefuseRate;
}
