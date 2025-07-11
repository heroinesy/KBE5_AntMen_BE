package com.antmen.antwork.common.api.response.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MatchingOverviewDto {
    private List<MatchingStatDto> stats;
    private List<ReservationMatchingListDto> reservations;
}
