package com.antmen.antwork.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AdminRefundStatisticsSummaryDto {
    private double refundRate;
    private Long totalRefundCount;
    private Long approveRefundCount;
    private Long totalRefundAmount;
}