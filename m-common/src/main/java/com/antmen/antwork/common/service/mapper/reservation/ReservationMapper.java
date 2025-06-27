package com.antmen.antwork.common.service.mapper.reservation;

import com.antmen.antwork.common.api.request.reservation.ReservationRequestDto;
import com.antmen.antwork.common.api.response.reservation.ReservationCommentResponseDto;
import com.antmen.antwork.common.api.response.reservation.ReservationResponseDto;
import com.antmen.antwork.common.domain.entity.reservation.*;
import com.antmen.antwork.common.domain.entity.account.User;
import com.antmen.antwork.common.infra.repository.account.CustomerAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservationMapper {

    private final CustomerAddressRepository customerAddressRepository;

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
                .reservationStatus(ReservationStatus.WAITING)
                .reservationMemo(dto.getReservationMemo())
                .reservationAmount(amount)
                .address(customerAddressRepository.findById(dto.getAddressId()).get())
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
                .managerName(entity.getManager() != null ? entity.getManager().getUserName() : null)
                .reservationCreatedAt(entity.getReservationCreatedAt())
                .reservationDate(entity.getReservationDate())
                .reservationTime(entity.getReservationTime())
                .categoryId(entity.getCategory() != null ? entity.getCategory().getCategoryId() : null)
                .categoryName(entity.getCategory() != null ? entity.getCategory().getCategoryName() : null)
                .reservationDuration(entity.getReservationDuration())
                .reservationStatus(entity.getReservationStatus().name())
                .reservationCancelReason(entity.getReservationCancelReason())
                .reservationMemo(entity.getReservationMemo())
                .reservationAmount(entity.getReservationAmount())
                .optionIds(optionIds)
                .optionNames(optionNames)
                .build();
    }

    public ReservationResponseDto toDto(Reservation reservation,
                                        List<ReservationOption> options) {
        return toDto(reservation, options, (short) 0);
    }

    public ReservationResponseDto toDto(Reservation entity) {
        return ReservationResponseDto.builder()
                .reservationId(entity.getReservationId())
                .reservationDate(entity.getReservationDate())
                .reservationTime(entity.getReservationTime())
                .reservationStatus(entity.getReservationStatus().name())
                .reservationAmount(entity.getReservationAmount())
                .reservationDuration(entity.getReservationDuration())
                .customerId(entity.getCustomer() != null ? entity.getCustomer().getUserId() : null)
                .managerId(entity.getManager() != null ? entity.getManager().getUserId() : null)
                .managerName(entity.getManager() != null ? entity.getManager().getUserName() : null)
                .categoryId(entity.getCategory() != null ? entity.getCategory().getCategoryId() : null)
                .categoryName(entity.getCategory() != null ? entity.getCategory().getCategoryName() : null)
                .reservationCreatedAt(entity.getReservationCreatedAt())
                .reservationMemo(entity.getReservationMemo())
                .build();
    }


    public ReservationCommentResponseDto toDto(ReservationComment entity) {
        return ReservationCommentResponseDto.builder()
                .reservationId(entity.getReservation().getReservationId())
                .checkinAt(entity.getCheckinAt())
                .checkoutAt(entity.getCheckoutAt())
                .comment(entity.getComment())
                .build();
    }
}
