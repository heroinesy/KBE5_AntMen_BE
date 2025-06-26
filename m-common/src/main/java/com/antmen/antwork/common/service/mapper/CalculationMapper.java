package com.antmen.antwork.common.service.mapper;

import com.antmen.antwork.common.api.response.calculation.CalculationResponseDto;
import com.antmen.antwork.common.domain.entity.reservation.Calculation;
import com.antmen.antwork.common.domain.entity.reservation.Reservation;
import org.springframework.stereotype.Component;

@Component
public class CalculationMapper {

    public CalculationResponseDto toDto(Reservation reservation, Calculation entity) {
        if (entity == null || reservation == null) return null;

        return CalculationResponseDto.builder()
                .calculationId(entity.getCalculationId())
                .managerId(reservation.getManager().getUserId())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .amount(entity.getAmount())
                .reservationId(reservation.getReservationId())
                .reservationDate(reservation.getReservationDate())
                .reservationAmount(reservation.getReservationAmount())
                .categoryName(reservation.getCategory().getCategoryName())
                .requestedAt(entity.getRequestedAt())
                .build();
    }

    public CalculationResponseDto toDto(Reservation reservation) {
        return CalculationResponseDto.builder()
                .managerId(reservation.getManager().getUserId())
                .reservationId(reservation.getReservationId())
                .categoryName(reservation.getCategory().getCategoryName())
                .reservationDate(reservation.getReservationDate())
                .reservationAmount(reservation.getReservationAmount())
                .build();
    }
}
