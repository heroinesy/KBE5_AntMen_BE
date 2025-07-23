package com.antmen.antwork.admin.api;

import com.antmen.antwork.common.api.response.CustomerReservationStatisticsDto;
import com.antmen.antwork.common.api.response.CustomerReviewInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDetailResponseDto {
    private AdminUserResponseDto userInfo;
    private CustomerReservationStatisticsDto reservationStatistics;
    private CustomerReviewInfoDto reviewInfo;
} 