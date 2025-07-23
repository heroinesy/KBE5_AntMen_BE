package com.antmen.antwork.common.api.response.reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ReservationStatDto {
    private String status;
    private long count;
}
