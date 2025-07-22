package com.antmen.antwork.domain.payment.dto;





@Getter
@Builder
@AllArgsConstructor
public class AdminManagerRefundStatisticsDto {
    private Long managerId;
    private String managerName;
    private Long refundCount;
    private Long totalRefundAmount;
}