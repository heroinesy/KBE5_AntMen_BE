package com.antmen.antwork.domain.payment.repository;












@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByReservation(Reservation reservation);

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

    List<Payment> findAllByPayCreatedTimeBetweenAndPayStatus(LocalDateTime start, LocalDateTime end, PaymentStatus payStatus);

    Payment findByReservation_ReservationId(Long reservationId);
} 