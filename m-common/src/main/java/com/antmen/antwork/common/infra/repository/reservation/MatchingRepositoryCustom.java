package com.antmen.antwork.common.infra.repository.reservation;

import com.antmen.antwork.common.domain.entity.reservation.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MatchingRepositoryCustom {
    Page<Reservation> findMatchingReservationByManagerId(Long userId, Pageable pageable);
}
