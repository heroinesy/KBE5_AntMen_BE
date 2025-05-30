package com.antmen.antwork.common.infra.repository;

import com.antmen.antwork.common.domain.entity.Matching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatchingRepository extends JpaRepository<Matching, Long> {

    Optional<Matching> findTopByReservationIdAndMatchingPriorityGreaterThanOrderByMatchingPriorityAsc(
            Long reservationId, int priority
    );

    List<Matching> findAllByMatchingMangerIsAcceptIsTrueAndMatchingIsFinalIsNull();

    List<Matching> findAllByReservationId(Long reservationId);

    @Query("SELECT m FROM Matching m " +
            "WHERE m.matchingIsRequest = true " +
            "AND m.matchingManagerIsAccept IS NULL " +
            "AND m.matchingUpdatedAt < :threshold " +
            "ORDER BY m.matchingPriority DESC " +
            "LIMIT 1" +
            "")
    List<Matching> findLatestPendingMatchings(@Param("threshold") LocalDateTime threshold);
}
