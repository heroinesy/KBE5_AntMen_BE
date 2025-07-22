package com.antmen.antwork.domain.reservation.dto;




@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationOptionRequestDto {
    private List<Long> categoryOptionIds;
}
