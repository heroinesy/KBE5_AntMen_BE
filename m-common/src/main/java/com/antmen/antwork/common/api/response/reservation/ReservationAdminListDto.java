package com.antmen.antwork.common.api.response.reservation;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationAdminListDto {
    Long reservationId;
    Long customerId;
    String customerName;
    String categoryName;
    String reservationStatus;
    LocalDateTime reservationCreatedAt;
    LocalDate reservationDate;
    LocalTime reservationTime;
}
