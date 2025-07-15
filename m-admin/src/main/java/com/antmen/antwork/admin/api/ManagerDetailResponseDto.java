package com.antmen.antwork.admin.api;

import com.antmen.antwork.common.api.response.ManagerMatchingStatisticsDto;
import com.antmen.antwork.common.api.response.ManagerWorkHistoryDto;
import com.antmen.antwork.common.api.response.ManagerReviewInfoDto;
import com.antmen.antwork.common.domain.entity.account.ManagerDetail;
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