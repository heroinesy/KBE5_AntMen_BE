package com.antmen.antwork.common.api.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ManagerMatchingStatisticsDto {
    private Long totalMatchings;
    private Long completedMatchings;
    private Long pendingMatchings;
    private Long cancelledMatchings;
    private Double successRate;
    private Double averageRating;
} 