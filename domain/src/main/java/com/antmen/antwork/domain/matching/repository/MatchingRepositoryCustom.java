package com.antmen.antwork.domain.matching.repository;

import com.antmen.antwork.domain.reservation.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MatchingRepositoryCustom {
    Page<Reservation> findMatchingReservationByManagerId(Long userId, Pageable pageable);
}
