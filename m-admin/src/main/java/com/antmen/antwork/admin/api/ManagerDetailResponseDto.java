package com.antmen.antwork.admin.api;

import com.antmen.antwork.domain.matching.dto.ManagerMatchingStatisticsDto;
import com.antmen.antwork.domain.reservation.dto.ManagerWorkHistoryDto;
import com.antmen.antwork.common.api.response.ManagerReviewInfoDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ManagerDetailResponseDto {
    private AdminUserResponseDto userInfo;
    private ManagerMatchingStatisticsDto matchingStatistics;
    private List<ManagerWorkHistoryDto> workHistory;
    private ManagerReviewInfoDto reviewInfo;
} 