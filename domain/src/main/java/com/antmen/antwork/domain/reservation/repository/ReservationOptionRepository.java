package com.antmen.antwork.domain.reservation.repository;







@Repository
public interface ReservationOptionRepository extends JpaRepository<ReservationOption, Long> {

     List<ReservationOption> findByReservation_ReservationId(Long reservationId);
}
