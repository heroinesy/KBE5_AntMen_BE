package com.antmen.antwork.domain.reservation.repository;










public interface ReservationRepositoryCustom {
    List<ReservationMatchingListDto> getReservationMatching(String matchingStatus, String searchName, String category, LocalDate startDate, LocalDate endDate);
    List<MatchingStatDto> getMatchingStat(String searchName, String category, LocalDate startDate, LocalDate endDate);
    List<ReservationStatDto> getCountOfReservationStatus(String searchName, String category, LocalDate startDate, LocalDate endDate);
    List<ReservationAdminListDto> getReservationAdminList(String reservationStatus, String searchName, String category, LocalDate startDate, LocalDate endDate);
}
