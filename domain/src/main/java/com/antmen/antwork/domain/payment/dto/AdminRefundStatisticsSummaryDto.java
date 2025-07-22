package com.antmen.antwork.domain.payment.dto;





@Getter
@Builder
@AllArgsConstructor
public class AdminRefundStatisticsSummaryDto {
    private double refundRate;
    private Long totalRefundCount;
    private Long approveRefundCount;
    private Long totalRefundAmount;
}