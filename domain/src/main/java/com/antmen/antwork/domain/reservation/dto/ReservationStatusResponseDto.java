package com.antmen.antwork.domain.reservation.dto;






@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationStatusResponseDto {
    private String reservationStatus;
    private Long count;
}
