package com.antmen.antwork.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminCustomerRefundStatisticsDto {
    private Long customerId;
    private String customerName;
    private Long refundCount;
    private Long totalRefundAmount;
}