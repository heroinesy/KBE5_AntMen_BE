package com.antmen.antwork.domain.calculation.dto;

import com.antmen.antwork.domain.calculation.entity.Calculation;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class AdminCalculationResponseDto {
    private Long calculationId;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime requestedAt;
    private int amount;

    private Long managerId;
    private String managerName;
    private String managerLoginId;

    private int totalReservationCount;
    private int totalReservationAmount;
    private List<AdminCalculationReservationDto> reservations;

    private static AdminCalculationResponseDto from(Calculation c, List<AdminCalculationReservationDto> reservations) {
        List<AdminCalculationReservationDto> reservationDtos = reservations.stream()
                .map(r -> AdminCalculationReservationDto.builder()
                        .reservationId(r.getReservationId())
                        .reservationDate(r.getReservationDate())
                        .categoryName(r.getCategoryName())
                        .optionNames(r.getOptionNames())
                        .reservationAmount(r.getReservationAmount()).build()
                ).toList();

        return AdminCalculationResponseDto.builder()
                .calculationId(c.getCalculationId())
                .startDate(c.getStartDate())
                .endDate(c.getEndDate())
                .requestedAt(c.getRequestedAt())
                .amount(c.getAmount())
                .managerId(c.getManager().getUserId())
                .managerName(c.getManager().getUserName())
                .managerLoginId(c.getManager().getUserLoginId())
                .totalReservationCount(reservationDtos.size())
                .totalReservationAmount(reservationDtos.stream().mapToInt(AdminCalculationReservationDto::getReservationAmount).sum())
                .reservations(reservationDtos)
                .build();
    }
}