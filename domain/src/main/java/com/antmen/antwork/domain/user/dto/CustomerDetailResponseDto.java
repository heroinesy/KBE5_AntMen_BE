package com.antmen.antwork.domain.user.dto;








@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDetailResponseDto {
    private AdminUserResponseDto userInfo;
    private CustomerReservationStatisticsDto reservationStatistics;
    private CustomerReviewInfoDto reviewInfo;
} 