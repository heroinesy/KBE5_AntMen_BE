package com.antmen.antwork.domain.calculation.dto;







@Getter
@Builder
public class AdminCalculationReservationDto {
    private Long reservationId;
    private LocalDate reservationDate;
    private String categoryName;
    private List<String> optionNames;
    private int reservationAmount;
}
