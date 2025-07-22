package com.antmen.antwork.domain.payment.dto;





@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRefundRequestDto {
    private Long reservationId;
    private String refundReason;
    private Integer refundAmount;
}