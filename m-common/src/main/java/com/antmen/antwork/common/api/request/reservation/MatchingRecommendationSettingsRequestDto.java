package com.antmen.antwork.common.api.request.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchingRecommendationSettingsRequestDto {

    private String firstPriority;  // distance, review, recent, workload, review_count
    private String secondPriority;
    private String thirdPriority;
    private String workloadPeriod; // 1week, 2week, 1month, 3month, 6month
} 