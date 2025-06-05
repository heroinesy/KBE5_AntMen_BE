package com.antmen.antwork.common.api.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaymentRequestDto {
    @NotNull
    private Long reservationId;        // 예약 번호 (엔티티와 타입 일치)
    @NotNull
    private String payMethod;          // 결제 수단
    @NotNull
    private double payAmount;          // 결제 금액
}
