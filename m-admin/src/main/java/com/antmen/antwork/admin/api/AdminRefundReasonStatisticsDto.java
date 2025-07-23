package com.antmen.antwork.admin.api;

import com.antmen.antwork.common.infra.repository.reservation.RefundRepository;
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