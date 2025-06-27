package com.antmen.antwork.common.api.request.reservation;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationCancelRequestDto {
    private String cancelReason;
} 