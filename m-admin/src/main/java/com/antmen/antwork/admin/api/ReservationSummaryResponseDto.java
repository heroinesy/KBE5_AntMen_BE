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
public class ReservationSummaryResponseDto {
    private Long totalCount;
    private Long cancelCount;
    private Long completeCount;
    private BigDecimal cancelRate;
    private BigDecimal avgUser;
}