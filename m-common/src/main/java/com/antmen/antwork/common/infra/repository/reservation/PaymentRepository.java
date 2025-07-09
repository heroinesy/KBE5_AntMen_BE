package com.antmen.antwork.common.infra.repository.reservation;

import com.antmen.antwork.common.domain.entity.reservation.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT SUM(payment.payAmount) " +
            "FROM Payment payment")
    Long findSum();

    @Query("SELECT SUM(payment.payAmount) " +
            "FROM Payment payment " +
            "WHERE MONTH(payment.pay_createdTime) = MONTH(CURRENT_DATE) " +
            "AND YEAR(payment.pay_createdTime) = YEAR(CURRENT_DATE)")
    Long findMonth();

    @Query(value = "SELECT COUNT(DISTINCT DATE(pay_created_time)) FROM payment", nativeQuery = true)
    Long findPaymentDays();
} 