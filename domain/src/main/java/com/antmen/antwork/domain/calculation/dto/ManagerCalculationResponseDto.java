package com.antmen.antwork.domain.calculation.dto;

import com.antmen.antwork.domain.calculation.entity.Calculation;
import com.antmen.antwork.domain.reservation.entity.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ManagerCalculationResponseDto {
    private Long calculationId;
    private Long managerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer amount;
    private Long reservationId;
    private LocalDate reservationDate;
    private Integer reservationAmount;
    private String categoryName;
    private LocalDateTime requestedAt;

    public static ManagerCalculationResponseDto from(Reservation reservation, Calculation entity) {
        if (entity == null || reservation == null) return null;

        return ManagerCalculationResponseDto.builder()
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

    // 매니저가 정산 요청 시 보여줄 예약 리스트
    public static ManagerCalculationResponseDto from(Reservation reservation) {
        return ManagerCalculationResponseDto.builder()
                .managerId(reservation.getManager().getUserId())
                .reservationId(reservation.getReservationId())
                .categoryName(reservation.getCategory().getCategoryName())
                .reservationDate(reservation.getReservationDate())
                .reservationAmount(reservation.getReservationAmount())
                .build();
    }
}