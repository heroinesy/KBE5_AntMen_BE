package com.antmen.antwork.common.infra.repository.reservation;

import com.antmen.antwork.common.domain.entity.reservation.Payment;
import com.antmen.antwork.common.domain.entity.reservation.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT SUM(payment.payAmount) " +
            "FROM Payment payment " +
            "WHERE payment.payStatus = 'DONE'")
    Long findSum();

    @Query("SELECT SUM(payment.payAmount) " +
            "FROM Payment payment " +
            "WHERE payment.payStatus = 'DONE'" +
            "AND MONTH(payment.payCreatedTime) = MONTH(CURRENT_DATE) " +
            "AND YEAR(payment.payCreatedTime) = YEAR(CURRENT_DATE)")
    Long findMonth();

    @Query(value = "SELECT COUNT(DISTINCT DATE(pay_created_time)) FROM payment WHERE pay_status = 'DONE'", nativeQuery = true)
    Long findPaymentDays();

    List<Payment> findAllByPayCreatedTimeBetween(LocalDateTime start, LocalDateTime end);

    List<Payment> findAllByPayCreatedTimeBetweenAndPayStatus(LocalDateTime start, LocalDateTime end, PaymentStatus payStatus);
} 