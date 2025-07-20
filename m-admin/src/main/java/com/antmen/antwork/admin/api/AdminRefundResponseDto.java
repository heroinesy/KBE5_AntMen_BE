package com.antmen.antwork.admin.api;

import com.antmen.antwork.domain.payment.entity.Refund;
import com.antmen.antwork.domain.payment.entity.RefundStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AdminRefundResponseDto {
    private Long payId;
    private Long userId;
    private String userName;
    private String userLoginId;
    private Long reservationId;
    private Integer refundAmount;
    private String refundReason;
    private String payMethod;
    private RefundStatus refundStatus;
    private LocalDateTime refundCreatedAt;
    private LocalDateTime refundProcessedAt;

    public static AdminRefundResponseDto from(Refund refund) {
        return AdminRefundResponseDto.builder()
                .payId(refund.getPayId())
                .userId(refund.getPayment().getReservation().getCustomer().getUserId())
                .userName(refund.getPayment().getReservation().getCustomer().getUserName())
                .userLoginId(refund.getPayment().getReservation().getCustomer().getUserLoginId())
                .reservationId(refund.getPayment().getReservation().getReservationId())
                .refundAmount(refund.getPayment().getPayAmount())
                .refundReason(refund.getRefundReason())
                .payMethod(refund.getPayment().getPayMethod())
                .refundStatus(refund.getRefundStatus())
                .refundCreatedAt(refund.getRefundCreatedAt())
                .refundProcessedAt(refund.getRefundProcessedAt())
                .build();
    }
}