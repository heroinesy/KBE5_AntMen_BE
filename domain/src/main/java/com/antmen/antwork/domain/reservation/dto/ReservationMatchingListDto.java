package com.antmen.antwork.domain.reservation.dto;










@Getter
@Setter
@Builder
@AllArgsConstructor
public class ReservationMatchingListDto {
    Long reservationId;
    Long customerId;
    String customerName;
    String categoryName;
    LocalDateTime reservationCreatedAt;
    LocalDate reservationDate;
    LocalTime reservationTime;
    long totalRequests;
    long totalManagerResponses;
    long totalManagerAccepts;
    String matchingStatus; // nothing, ing, fail
}
