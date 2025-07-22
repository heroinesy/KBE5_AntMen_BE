package com.antmen.antwork.domain.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ReservationAdminOverviewDto {
    List<ReservationStatDto> reservationStatDtoList;
    List<ReservationAdminListDto> reservationAdminListDtos;
}