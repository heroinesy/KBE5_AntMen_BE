package com.antmen.antwork.admin.api;

import com.antmen.antwork.domain.payment.repository.RefundRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminRefundReasonStatisticsDto {
    private String refundReason;
    private Long count;

    public static AdminRefundReasonStatisticsDto from(RefundRepository.RefundReasonStatisticsDto p) {
        return new AdminRefundReasonStatisticsDto(p.getReason(), p.getCount());
    }
}