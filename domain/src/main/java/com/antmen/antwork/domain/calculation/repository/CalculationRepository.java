package com.antmen.antwork.domain.calculation.repository;










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