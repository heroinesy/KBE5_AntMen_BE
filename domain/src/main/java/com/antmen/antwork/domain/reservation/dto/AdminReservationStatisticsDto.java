package com.antmen.antwork.domain.reservation.dto;








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