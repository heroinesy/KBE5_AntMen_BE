package com.antmen.antwork.common.api.response.reservation;

import com.antmen.antwork.common.domain.entity.reservation.Refund;
import com.antmen.antwork.common.domain.entity.reservation.RefundStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RefundResponseDto {
    private Long payId;
    private Long userId;
    private double refundAmount;
    private String refundReason;
    private RefundStatus refundStatus;
    private LocalDateTime refundCreatedAt;
    private LocalDateTime refundProcessedAt;

    public static RefundResponseDto from(Refund refund) {
        return RefundResponseDto.builder()
                .payId(refund.getPayId())
                .userId(refund.getPayment().getReservation().getCustomer().getUserId())
                .refundAmount(refund.getRefundAmount())
                .refundReason(refund.getRefundReason())
                .refundStatus(refund.getRefundStatus())
                .refundCreatedAt(refund.getRefundCreatedAt())
                .refundProcessedAt(refund.getRefundProcessedAt())
                .build();
    }
}
