package com.antmen.antwork.domain.reservation.dto;








@Getter
@Setter
@Builder
@AllArgsConstructor
public class ReservationAdminOverviewDto {
    List<ReservationStatDto> reservationStatDtoList;
    List<ReservationAdminListDto> reservationAdminListDtos;
}
