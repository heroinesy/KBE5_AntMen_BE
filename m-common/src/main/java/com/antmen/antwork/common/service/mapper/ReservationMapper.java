package com.antmen.antwork.common.service.mapper;

import com.antmen.antwork.common.api.request.ReservationRequestDto;
import com.antmen.antwork.common.api.response.ReservationResponseDto;
import com.antmen.antwork.common.domain.entity.Reservation;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {
    public Reservation toEntity(ReservationRequestDto dto) {
        if (dto == null) return null;
        return Reservation.builder()
                .customerId(dto.getCustomerId())
                .reservationCreatedAt(dto.getReservationCreatedAt())
                .reservationDate(dto.getReservationDate())
                .reservationTime(dto.getReservationTime())
                .category(dto.getCategory())
                .reservationDuration(dto.getReservationDuration())
                .reservationMeno(dto.getReservationMeno())
                .reservationAmount(dto.getReservationAmount())
                .build();
    }

    public ReservationResponseDto toDto(Reservation entity) {
        if (entity == null) return null;
        return ReservationResponseDto.builder()
                .reservationId(entity.getReservationId())
                .customerId(entity.getCustomerId())
                .reservationCreatedAt(entity.getReservationCreatedAt())
                .reservationDate(entity.getReservationDate())
                .reservationTime(entity.getReservationTime())
                .category(entity.getCategory())
                .reservationDuration(entity.getReservationDuration())
                .managerId(entity.getManagerId())
                .managerAcceptTime(entity.getManagerAcceptTime())
                .reservationStatus(entity.getReservationStatus())
                .reservationCancelReason(entity.getReservationCancelReason())
                .reservationMeno(entity.getReservationMeno())
                .reservationAmount(entity.getReservationAmount())
                .build();
    }
}
