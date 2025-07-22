package com.antmen.antwork.domain.matching.dto;








@Getter
@Setter
@AllArgsConstructor
public class MatchingOverviewDto {
    private List<MatchingStatDto> stats;
    private List<ReservationMatchingListDto> reservations;
}
