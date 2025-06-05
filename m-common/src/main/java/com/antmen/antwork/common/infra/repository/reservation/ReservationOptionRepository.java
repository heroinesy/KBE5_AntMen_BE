package com.antmen.antwork.common.infra.repository.reservation;

import com.antmen.antwork.common.domain.entity.reservation.ReservationOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationOptionRepository extends JpaRepository<ReservationOption, Long> {

     List<ReservationOption> findByReservation_ReservationId(Long reservationId);
}
