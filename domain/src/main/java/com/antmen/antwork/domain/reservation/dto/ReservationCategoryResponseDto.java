package com.antmen.antwork.domain.reservation.dto;






@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationCategoryResponseDto {
    private String categoryName;
    private Long categoryCount;
}
