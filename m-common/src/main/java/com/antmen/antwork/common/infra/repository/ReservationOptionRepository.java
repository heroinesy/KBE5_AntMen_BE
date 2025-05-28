package com.antmen.antwork.common.infra.repository;

import com.antmen.antwork.common.domain.entity.ReservationOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationOptionRepository extends JpaRepository<ReservationOption, Long> {

     List<ReservationOption> findByReservationId(Long reservationId);
     void deleteByReservationId(Long reservationId);
}
