package com.antmen.antwork.admin.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AdminManagerRefundStatisticsDto {
    private Long managerId;
    private Long managerName;
    private Long refundCount;
    private Long totalRefundAmount;

    public static AdminManagerRefundStatisticsDto from(Long managerId, Long managerName, Long refundCount, Long totalRefundAmount) {
        return AdminManagerRefundStatisticsDto.builder()
                .managerId(managerId)
                .managerName(managerName)
                .refundCount(refundCount)
                .totalRefundAmount(totalRefundAmount)
                .build();
    }
}