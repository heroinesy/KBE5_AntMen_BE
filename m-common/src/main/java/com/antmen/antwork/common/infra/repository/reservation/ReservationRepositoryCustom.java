package com.antmen.antwork.common.infra.repository.reservation;

import com.antmen.antwork.common.api.response.reservation.MatchingStatDto;
import com.antmen.antwork.common.api.response.reservation.ReservationAdminListDto;
import com.antmen.antwork.common.api.response.reservation.ReservationMatchingListDto;
import com.antmen.antwork.common.api.response.reservation.ReservationStatDto;
import com.antmen.antwork.common.domain.entity.reservation.Reservation;
import org.springframework.data.jpa.repository.EntityGraph;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepositoryCustom {
    List<ReservationMatchingListDto> getReservationMatching(String matchingStatus, String searchName, String category, LocalDate startDate, LocalDate endDate);
    List<MatchingStatDto> getMatchingStat(String searchName, String category, LocalDate startDate, LocalDate endDate);
    List<ReservationStatDto> getCountOfReservationStatus(String searchName, String category, LocalDate startDate, LocalDate endDate);
    List<ReservationAdminListDto> getReservationAdminList(String reservationStatus, String searchName, String category, LocalDate startDate, LocalDate endDate);

    List<ReservationAdminListDto> getReservationAdminListDSL(String reservationStatus, String searchName, String category, LocalDate startDate, LocalDate endDate);
}
