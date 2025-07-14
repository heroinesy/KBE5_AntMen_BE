package com.antmen.antwork.common.infra.repository.reservation;

import com.antmen.antwork.common.domain.entity.account.User;
import com.antmen.antwork.common.domain.entity.reservation.Matching;
import com.antmen.antwork.common.domain.entity.reservation.Reservation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatchingRepository extends JpaRepository<Matching, Long>, MatchingRepositoryCustom {

    Optional<Matching> findTopByReservation_ReservationIdAndMatchingPriorityGreaterThanOrderByMatchingPriorityAsc(
            Long reservationId, int priority);

    List<Matching> findAllByMatchingManagerIsAcceptIsTrueAndMatchingIsFinalIsNull();

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

    List<Matching> findAllByManagerAndMatchingManagerIsAcceptTrue(User manager);

    List<Matching> findAllByManagerAndMatchingIsRequestTrue(User manager);

    Matching findFirstByReservationOrderByMatchingUpdatedAtDesc(Reservation reservation);

    long countByReservationAndMatchingIsRequest(Reservation reservation, Boolean matchingIsRequest);

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

//    @Query("SELECT COUNT(m) FROM Matching m ")
//    Long countMatching();

//    @Query("SELECT COUNT(m) FROM Matching m WHERE m.matchingIsFinal = true")
//    Long countSuccess();

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
    Long countByMatchingIsFinalTrue();
}
