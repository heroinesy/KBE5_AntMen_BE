package com.antmen.antwork.admin.api;

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