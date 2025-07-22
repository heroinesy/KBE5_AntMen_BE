package com.antmen.antwork.domain.matching.repository;
















@Repository
public interface MatchingRepository extends JpaRepository<Matching, Long>, MatchingRepositoryCustom {

    Optional<Matching> findTopByReservation_ReservationIdAndMatchingPriorityGreaterThanOrderByMatchingPriorityAsc(
            Long reservationId, int priority);

    List<Matching> findAllByReservation_ReservationId(Long reservationId);

    @Query("""
        SELECT m FROM Matching m
        WHERE m.matchingIsRequest = true
          AND m.matchingIsFinal IS NULL
          AND m.matchingUpdatedAt < :threshold
          AND m.reservation.reservationStatus = 'WAITING'
          AND m.reservation.address.addressId >= :minAddressId
    """)
    List<Matching> findLatestPendingMatchings(
            @Param("threshold") LocalDateTime threshold,
            @Param("minAddressId") Long minAddressId
    );

    @Query(
            "SELECT count(m.matchingId) " +
                    "from Matching m " +
                    "where m.reservation = :reservation " +
                    "and ( " +
                    "      m.matchingManagerIsAccept = false " +
                    "  or  m.matchingIsFinal = false" +
                    ")"
    )
    long countTheResponse(Reservation reservation);

    /**
     * 특정 예약에 대한 가장 높은 매칭 우선순위 조회
     */
    @Query("SELECT COALESCE(MAX(m.matchingPriority), 0) FROM Matching m WHERE m.reservation.reservationId = :reservationId")
    Integer findMaxMatchingPriorityByReservationId(@Param("reservationId") Long reservationId);

    interface MatchingTopManagerProjection {
        Long getManagerId();
        String getManagerName();
        Long getSuccessCount();
    }
    @Query("""
    SELECT
        m.manager.userId AS managerId,
        m.manager.userName AS managerName,
        COUNT(m) AS successCount
    FROM Matching m
    WHERE m.matchingIsFinal = true
    GROUP BY m.manager.userId, m.manager.userName
    ORDER BY successCount DESC
    """)
    List<MatchingTopManagerProjection> findTopManagers(Pageable pageable);

    @Query("SELECT COUNT(m) FROM Matching m WHERE m.matchingIsFinal = true")
    Long countSuccess();

    interface DailyMatchingStatisticsProjection {
        LocalDate getDate();
        Long getRequestCount();
        Long getSuccessCount();
    }

    @Query("""
    SELECT
        FUNCTION('DATE', m.matchingUpdatedAt) AS date,
        COUNT(m) AS requestCount,
        SUM(CASE WHEN m.matchingIsFinal = true THEN 1 ELSE 0 END) AS successCount
    FROM Matching m
    WHERE m.matchingUpdatedAt BETWEEN :start AND :end
    GROUP BY FUNCTION('DATE', m.matchingUpdatedAt)
    ORDER BY FUNCTION('DATE', m.matchingUpdatedAt)
    """)
    List<DailyMatchingStatisticsProjection> findDailyMatchingStats(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("""
    SELECT COUNT(m)
    FROM Matching m
    WHERE m.matchingManagerIsAccept = true
    AND (m.matchingIsFinal IS NULL OR m.matchingIsFinal = false)
""")
    Long countRefusedByCustomer();

    @Query("""
    SELECT COUNT(m)
    FROM Matching m
    WHERE m.matchingIsRequest = true
    AND (m.matchingManagerIsAccept IS NULL OR m.matchingManagerIsAccept = false)
""")
    Long countRefusedByManager();

    @Query("SELECT COUNT(m) FROM Matching m")
    Long countAll();
}