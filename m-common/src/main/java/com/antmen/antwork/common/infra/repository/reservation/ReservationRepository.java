package com.antmen.antwork.common.infra.repository.reservation;

import com.antmen.antwork.common.domain.entity.account.User;
import com.antmen.antwork.common.domain.entity.reservation.Reservation;
import com.antmen.antwork.common.domain.entity.reservation.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByCustomer_UserId(Long UserId);
    List<Reservation> findByManager_UserId(Long UserId);
    List<Reservation> findAllByManager(User manager);

    Page<Reservation> findByReservationStatus(ReservationStatus reservationStatus, Pageable pageable);
    List<Reservation> findByReservationStatusAndManager_UserId(ReservationStatus reservationStatus, Long userId);

    List<Reservation> findByReservationStatusAndManager_UserIdAndReservationDateBetween(ReservationStatus reservationStatus, Long managerId, LocalDate weekStart, LocalDate weekEnd);
}
