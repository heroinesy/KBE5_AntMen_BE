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
public class AdminMatchingStatisticsSummaryDto {
    private MatchingSummaryResponseDto matchingSummary;
    private List<MatchingTopManagerDto> topManagerList;
}