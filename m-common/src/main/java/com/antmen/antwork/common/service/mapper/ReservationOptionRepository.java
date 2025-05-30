package com.antmen.antwork.common.service.mapper;

import com.antmen.antwork.common.domain.entity.ReservationOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface ReservationOptionRepository extends JpaRepository<ReservationOption, Long> {
    List<ReservationOption> findByReservation_ReservationId(Long reservationReservationId);
}
