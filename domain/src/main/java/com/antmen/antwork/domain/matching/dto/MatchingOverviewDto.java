package com.antmen.antwork.domain.matching.dto;

import com.antmen.antwork.domain.reservation.dto.ReservationMatchingListDto;
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
