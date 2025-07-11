package com.antmen.antwork.common.infra.repository.reservation;

import com.antmen.antwork.common.domain.entity.reservation.Calculation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CalculationRepository extends JpaRepository<Calculation, Long> {
    List<Calculation> findAllByManager_UserId(Long userId);

    @Query("SELECT SUM(caculation.amount) " +
            "FROM Calculation caculation")
    Long findSum();

    @Query("SELECT SUM(calculation.amount) " +
            "FROM Calculation calculation " +
            "WHERE calculation.requestedAt BETWEEN :start AND :end")
    Long findAmountByRequestedAtBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    List<Calculation> findByRequestedAtBetween(LocalDateTime start, LocalDateTime end);
}