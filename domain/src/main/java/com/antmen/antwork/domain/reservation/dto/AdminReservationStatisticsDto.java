package com.antmen.antwork.domain.reservation.dto;

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
    private List<ReservationStatusResponseDto> reservationStatus;
    private List<DailyReservationResponseDto> dailyList;
    private List<ReservationCategoryResponseDto> categoryList;
}