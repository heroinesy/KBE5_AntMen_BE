package com.antmen.antwork.domain.matching.dto;




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