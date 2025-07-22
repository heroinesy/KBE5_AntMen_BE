package com.antmen.antwork.domain.matching.dto;








@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminMatchingStatisticsSummaryDto {
    private MatchingSummaryResponseDto matchingSummary;
    private List<MatchingTopManagerDto> topManagerList;
    private List<DailyMatchingResponseDto> dailyMatchingList;
}