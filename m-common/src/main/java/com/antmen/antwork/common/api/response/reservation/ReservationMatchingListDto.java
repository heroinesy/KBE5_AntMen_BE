package com.antmen.antwork.common.api.response.reservation;

import com.antmen.antwork.common.domain.entity.reservation.Matching;
import com.antmen.antwork.common.domain.entity.reservation.Reservation;
import com.antmen.antwork.common.domain.entity.reservation.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ReservationMatchingListDto {
    Long reservationId;
    Long customerId;
    String customerName;
    String categoryName;
    LocalDate reservationDate;
    LocalTime reservationTime;
    long totalRequests;
    long totalManagerResponses;
    long totalManagerAccepts;
    String matchingStatus; // nothing, ing, fail
}
