package com.antmen.antwork.domain.payment.dto;




@Getter
@AllArgsConstructor
public class AdminCustomerRefundStatisticsDto {
    private Long customerId;
    private String customerName;
    private Long refundCount;
    private Long totalRefundAmount;
}