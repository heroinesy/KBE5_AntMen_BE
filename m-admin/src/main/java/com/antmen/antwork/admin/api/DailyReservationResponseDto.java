package com.antmen.antwork.admin.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
