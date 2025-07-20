package com.antmen.antwork.domain.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationCategoryResponseDto {
    private String categoryName;
    private Long categoryCount;
}
