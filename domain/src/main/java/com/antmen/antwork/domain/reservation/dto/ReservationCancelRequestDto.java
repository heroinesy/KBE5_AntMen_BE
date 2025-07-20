package com.antmen.antwork.domain.reservation.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationCancelRequestDto {
    private String cancelReason;
} 