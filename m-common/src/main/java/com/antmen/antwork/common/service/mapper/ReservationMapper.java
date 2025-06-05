package com.antmen.antwork.common.service.mapper;

import com.antmen.antwork.common.api.request.ReservationRequestDto;
import com.antmen.antwork.common.api.response.ReservationResponseDto;
import com.antmen.antwork.common.domain.entity.Category;
import com.antmen.antwork.common.domain.entity.Reservation;
import com.antmen.antwork.common.domain.entity.ReservationOption;
import com.antmen.antwork.common.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservationMapper {

    public Reservation toEntity(ReservationRequestDto dto,
                                User customer,
                                Category category,
                                short duration,
                                int amount
    ) {
        if (dto == null || customer == null || category == null) return null;
        return Reservation.builder()
                .customer(customer)
                .category(category)
                .reservationCreatedAt(dto.getReservationCreatedAt())
                .reservationDate(dto.getReservationDate())
                .reservationTime(dto.getReservationTime())
                .reservationDuration(duration)
                .reservationMemo(dto.getReservationMemo())
                .reservationAmount(amount)
                .build();
    }

    public ReservationResponseDto toDto(Reservation entity,
                                        List<ReservationOption> options,
                                        short recommendDuration) {
        List<Long> optionIds = options.stream()
                .map(opt -> opt.getCategoryOption().getCoId())
                .toList();

        List<String> optionNames = options.stream()
                .map(opt -> opt.getCategoryOption().getCoName())
                .toList();

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
                .matchedAt(entity.getMatchedAt())
                .reservationStatus(entity.getReservationStatus().name()) // "WAITING", "MATCHING"
                .reservationCancelReason(entity.getReservationCancelReason())
                .reservationMemo(entity.getReservationMemo())
                .reservationAmount(entity.getReservationAmount())
                .recommendDuration(recommendDuration)
                .build();
    }

    public ReservationResponseDto toDto(Reservation reservation,
                                        List<ReservationOption> options) {
        return toDto(reservation, options, (short) 0);
    }
}
