package com.antmen.antwork.domain.reservation.repository;

import com.antmen.antwork.domain.reservation.entity.ReservationOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationOptionRepository extends JpaRepository<ReservationOption, Long> {

     List<ReservationOption> findByReservation_ReservationId(Long reservationId);
}
