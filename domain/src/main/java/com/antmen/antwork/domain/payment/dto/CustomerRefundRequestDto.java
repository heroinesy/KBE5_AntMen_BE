package com.antmen.antwork.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRefundRequestDto {
    private Long reservationId;
    private String refundReason;
    private Integer refundAmount;
}