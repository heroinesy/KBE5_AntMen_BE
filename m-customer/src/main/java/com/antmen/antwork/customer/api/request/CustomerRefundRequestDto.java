package com.antmen.antwork.customer.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRefundRequestDto {
    private Long payId;
    private String refundReason;
    private Integer refundAmount;
}