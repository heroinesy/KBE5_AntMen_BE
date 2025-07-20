package com.antmen.antwork.domain.matching.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchingTopManagerDto {
    private Long managerId;
    private String managerName;
    private Long successCount;
}