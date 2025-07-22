package com.antmen.antwork.domain.reservation.dto;








@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyReservationResponseDto {
    private LocalDate date;
    private Long dailyReservationsCount;
    private Long dailyCancelCount;
    private Long dailyCompletedCount;
    private Long dailyMatchingCount;
}
