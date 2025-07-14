package com.antmen.antwork.admin.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminReviewStatisticsDto {
    private Long totalReviewCount;
    private BigDecimal avgReviewSatisfaction;
    private List<ReviewSatisfactionDto> topCustomerList;
    private List<ReviewSatisfactionDto> topManagerList;
}