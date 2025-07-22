package com.antmen.antwork.domain.payment.dto;

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