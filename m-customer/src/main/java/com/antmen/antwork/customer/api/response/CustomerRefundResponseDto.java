package com.antmen.antwork.customer.api.response;

import com.antmen.antwork.domain.payment.entity.Refund;
import com.antmen.antwork.domain.payment.entity.RefundStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRefundResponseDto {
    private Long payId;
    private String refundReason;
    private Integer refundAmount;
    private RefundStatus refundStatus;
    private LocalDateTime refundCreatedAt;

    public static CustomerRefundResponseDto from(Refund refund) {
        return CustomerRefundResponseDto.builder()
                .payId(refund.getPayId())
                .refundReason(refund.getRefundReason())
                .refundAmount(refund.getRefundAmount())
                .refundStatus(refund.getRefundStatus())
                .refundCreatedAt(refund.getRefundCreatedAt())
                .build();
    }
}