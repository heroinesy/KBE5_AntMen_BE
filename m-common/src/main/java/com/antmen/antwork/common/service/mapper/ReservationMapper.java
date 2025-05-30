package com.antmen.antwork.common.service.mapper;

import com.antmen.antwork.common.api.request.ReservationRequestDto;
import com.antmen.antwork.common.api.response.ReservationResponseDto;
import com.antmen.antwork.common.domain.entity.Category;
import com.antmen.antwork.common.domain.entity.Reservation;
import com.antmen.antwork.common.domain.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {
    public Reservation toEntity(ReservationRequestDto dto, User customer, Category category) {
        if (dto == null || customer == null || category == null) return null;
        return Reservation.builder()
                .customer(customer)
                .category(category)
                .reservationCreatedAt(dto.getReservationCreatedAt())
                .reservationDate(dto.getReservationDate())
                .reservationTime(dto.getReservationTime())
                .reservationDuration(dto.getReservationDuration())
                .reservationMeno(dto.getReservationMeno())
                .reservationAmount(dto.getReservationAmount())
                .build();
    }

    public ReservationResponseDto toDto(Reservation entity) {
        if (entity == null) return null;
        return ReservationResponseDto.builder()
                .reservationId(entity.getReservationId())
                .customerId(entity.getCustomer() != null ? entity.getCustomer().getUserId() : null)
                .managerId(entity.getManager() != null ? entity.getManager().getUserId() : null)
                .reservationCreatedAt(entity.getReservationCreatedAt())
                .reservationDate(entity.getReservationDate())
                .reservationTime(entity.getReservationTime())
                .categoryId(entity.getCategory() != null ? entity.getCategory().getCategoryId() : null)
                .categoryName(entity.getCategory() != null ? entity.getCategory().getCategoryName() : null)
                .reservationDuration(entity.getReservationDuration())
                .managerAcceptTime(entity.getManagerAcceptTime())
                .reservationStatus(entity.getReservationStatus())
                .reservationCancelReason(entity.getReservationCancelReason())
                .reservationMeno(entity.getReservationMeno())
                .reservationAmount(entity.getReservationAmount())
                .build();
    }
}
