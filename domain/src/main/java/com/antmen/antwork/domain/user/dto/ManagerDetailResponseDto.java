package com.antmen.antwork.domain.user.dto;









@Getter
@Builder
public class ManagerDetailResponseDto {
    private AdminUserResponseDto userInfo;
    private ManagerMatchingStatisticsDto matchingStatistics;
    private List<ManagerWorkHistoryDto> workHistory;
    private ManagerReviewInfoDto reviewInfo;
} 