package com.antmen.antwork.domain.reservation.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationStatusChangeRequestDto {
    private String status;
    private String reason;
}