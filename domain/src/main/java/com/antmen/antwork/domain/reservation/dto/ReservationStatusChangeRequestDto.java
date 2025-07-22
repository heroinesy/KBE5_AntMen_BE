package com.antmen.antwork.domain.reservation.dto;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationStatusChangeRequestDto {
    private String status;
    private String reason;
} 