package com.antmen.antwork.common.api.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationOptionResponseDto {
    private Long reservationId;         // 예약 번호
    private Long categoryOptionId;      // 옵션 번호
    private String coName;              // 옵션 이름
    private Integer coPrice;            // 옵션 가격
    private Short coTime;               // 옵션 시간
}
