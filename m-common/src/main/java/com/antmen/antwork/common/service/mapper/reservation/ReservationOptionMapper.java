package com.antmen.antwork.common.service.mapper.reservation;

import com.antmen.antwork.common.api.response.reservation.ReservationOptionResponseDto;
import com.antmen.antwork.common.domain.entity.reservation.CategoryOption;
import com.antmen.antwork.common.domain.entity.reservation.Reservation;
import com.antmen.antwork.common.domain.entity.reservation.ReservationOption;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReservationOptionMapper {

    public ReservationOption toEntity(Reservation reservation, CategoryOption categoryOption) {
        return ReservationOption.builder()
                .reservation(reservation)
                .categoryOption(categoryOption)
                .build();
    }

    public ReservationOptionResponseDto toResponseDto(ReservationOption entity) {
        return ReservationOptionResponseDto.builder()
                .reservationId(entity.getReservation().getReservationId())
                .categoryOptionId(entity.getCategoryOption().getCoId())
                .coName(entity.getCategoryOption().getCoName())
                .coPrice(entity.getCategoryOption().getCoPrice())
                .coTime(entity.getCategoryOption().getCoTime())
                .build();
    }

    public List<ReservationOptionResponseDto> toResponseDtoList(List<ReservationOption> entities) {
        return entities.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }
}



