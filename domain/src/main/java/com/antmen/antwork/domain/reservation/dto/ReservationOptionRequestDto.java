package com.antmen.antwork.domain.reservation.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationOptionRequestDto {
    private List<Long> categoryOptionIds;
}