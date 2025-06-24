package com.antmen.antwork.common.api.request.reservation;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationStatusChangeRequestDto {
    private String status;
} 