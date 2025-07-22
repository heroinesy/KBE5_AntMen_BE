package com.antmen.antwork.domain.reservation.dto;







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
