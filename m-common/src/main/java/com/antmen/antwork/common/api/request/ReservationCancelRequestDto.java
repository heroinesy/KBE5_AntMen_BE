package com.antmen.antwork.common.api.request;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@Builder
public class ReservationCancelRequestDto {
    private String cancelReason;
} 