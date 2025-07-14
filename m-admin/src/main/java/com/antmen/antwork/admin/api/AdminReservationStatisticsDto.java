package com.antmen.antwork.admin.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminReservationStatisticsDto {
    private ReservationSummaryResponseDto reservationSummary;
    private List<DailyReservationResponseDto> dailyList;
    private List<ReservationCategoryResponseDto> categoryList;
}