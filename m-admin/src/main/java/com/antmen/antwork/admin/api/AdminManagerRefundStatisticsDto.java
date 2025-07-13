package com.antmen.antwork.admin.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AdminManagerRefundStatisticsDto {
    private Long managerId;
    private String managerName;
    private Long refundCount;
    private Long totalRefundAmount;
}