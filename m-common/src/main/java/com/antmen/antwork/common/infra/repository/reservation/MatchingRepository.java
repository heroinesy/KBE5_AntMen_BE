package com.antmen.antwork.common.infra.repository.reservation;

import com.antmen.antwork.common.domain.entity.account.User;
import com.antmen.antwork.common.domain.entity.reservation.Matching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatchingRepository extends JpaRepository<Matching, Long> {

    Optional<Matching> findTopByReservation_ReservationIdAndMatchingPriorityGreaterThanOrderByMatchingPriorityAsc(
            Long reservationId, int priority);

    List<Matching> findAllByMatchingManagerIsAcceptIsTrueAndMatchingIsFinalIsNull();

    List<Matching> findAllByReservation_ReservationId(Long reservationId);

    @Query("SELECT m FROM Matching m " +
            "WHERE m.matchingIsRequest = true " +
            "AND m.matchingManagerIsAccept IS NULL " +
            "AND m.matchingUpdatedAt < :threshold " +
            "ORDER BY m.matchingPriority DESC " +
            "LIMIT 1" +
            "")
    List<Matching> findLatestPendingMatchings(@Param("threshold") LocalDateTime threshold);

    List<Matching> findAllByManagerAndMatchingManagerIsAcceptTrue(User manager);

    List<Matching> findAllByManagerAndMatchingIsRequestTrue(User manager);
}
