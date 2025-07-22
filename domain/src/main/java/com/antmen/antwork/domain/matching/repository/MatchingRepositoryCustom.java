package com.antmen.antwork.domain.matching.repository;





public interface MatchingRepositoryCustom {
    Page<Reservation> findMatchingReservationByManagerId(Long userId, Pageable pageable);
}
