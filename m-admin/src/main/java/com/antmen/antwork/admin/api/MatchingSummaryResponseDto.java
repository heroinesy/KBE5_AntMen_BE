package com.antmen.antwork.admin.api;

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
