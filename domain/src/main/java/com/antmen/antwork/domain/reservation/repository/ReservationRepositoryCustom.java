package com.antmen.antwork.domain.reservation.repository;

import com.antmen.antwork.domain.matching.dto.MatchingStatDto;
import com.antmen.antwork.domain.reservation.dto.ReservationAdminListDto;
import com.antmen.antwork.domain.reservation.dto.ReservationMatchingListDto;
import com.antmen.antwork.domain.reservation.dto.ReservationStatDto;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepositoryCustom {
    List<ReservationMatchingListDto> getReservationMatching(String matchingStatus, String searchName, String category, LocalDate startDate, LocalDate endDate);
    List<MatchingStatDto> getMatchingStat(String searchName, String category, LocalDate startDate, LocalDate endDate);
    List<ReservationStatDto> getCountOfReservationStatus(String searchName, String category, LocalDate startDate, LocalDate endDate);
    List<ReservationAdminListDto> getReservationAdminList(String reservationStatus, String searchName, String category, LocalDate startDate, LocalDate endDate);
}
