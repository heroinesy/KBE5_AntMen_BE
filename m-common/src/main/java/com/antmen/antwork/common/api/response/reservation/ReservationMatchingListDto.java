package com.antmen.antwork.common.api.response.reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
