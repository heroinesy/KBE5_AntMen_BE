package com.antmen.antwork.common.service.mapper;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import com.antmen.antwork.common.api.request.ReservationRequestDto;
import com.antmen.antwork.common.api.response.ReservationResponseDto;
import com.antmen.antwork.common.domain.entity.Reservation;
import com.antmen.antwork.common.domain.entity.User;
import com.antmen.antwork.common.infra.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class ReservationMapper {
    private final UserRepository userRepository;

    public Reservation toEntity(ReservationRequestDto dto) {
        if (dto == null)
            return null;
        User customer = userRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("고객을 찾을 수 없습니다."));

        return Reservation.builder()
                .customer(customer)
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
        if (entity == null)
            return null;
        return ReservationResponseDto.builder()
                .reservationId(entity.getReservationId())
                .customerId(entity.getCustomer() != null ? entity.getCustomer().getUserId() : null)
                .reservationCreatedAt(entity.getReservationCreatedAt())
                .reservationDate(entity.getReservationDate())
                .reservationTime(entity.getReservationTime())
                .category(entity.getCategory())
                .reservationDuration(entity.getReservationDuration())
                .managerId(entity.getManager() != null ? entity.getManager().getUserId() : null)
                .managerAcceptTime(entity.getManagerAcceptTime())
                .reservationStatus(entity.getReservationStatus())
                .reservationCancelReason(entity.getReservationCancelReason())
                .reservationMeno(entity.getReservationMeno())
                .reservationAmount(entity.getReservationAmount())
                .build();
    }
}
