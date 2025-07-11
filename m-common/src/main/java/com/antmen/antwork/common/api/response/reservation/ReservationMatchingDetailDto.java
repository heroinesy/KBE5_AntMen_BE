package com.antmen.antwork.common.api.response.reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ReservationMatchingDetailDto {
    private int customerAge;
    private String customerGender;
    private String customerPhone;
    private String customerEmail;
    private String reservationAddress;
    private short reservationDuration;

    List<MatchingDto> matchingDtoList;
}
