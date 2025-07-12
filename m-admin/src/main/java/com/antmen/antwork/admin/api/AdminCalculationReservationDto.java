package com.antmen.antwork.admin.api;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class AdminCalculationReservationDto {
    private Long reservationId;
    private LocalDate reservationDate;
    private String categoryName;
    private List<String> optionNames;
    private int reservationAmount;
}
