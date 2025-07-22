package com.antmen.antwork.domain.payment.dto;






@Getter
@Setter
@Builder
public class PaymentRequestDto {
    @NotNull
    private Long reservationId;        // 예약 번호 (엔티티와 타입 일치)
    @NotNull
    private String payMethod;          // 결제 수단
    @NotNull
    private Integer payAmount;          // 결제 금액
}
