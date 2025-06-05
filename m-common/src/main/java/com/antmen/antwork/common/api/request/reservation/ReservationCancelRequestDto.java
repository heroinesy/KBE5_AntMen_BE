package com.antmen.antwork.common.api.request.reservation;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@Builder
public class ReservationCancelRequestDto {
    private String cancelReason;
} 