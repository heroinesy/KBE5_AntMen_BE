package com.antmen.antwork.domain.user.dto;

import com.antmen.antwork.domain.reservation.dto.CustomerReservationStatisticsDto;
import com.antmen.antwork.domain.review.dto.CustomerReviewInfoDto;
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