package com.antmen.antwork.common.infra.repository.reservation;

import com.antmen.antwork.common.domain.entity.account.User;
import com.antmen.antwork.common.domain.entity.reservation.Reservation;
import com.antmen.antwork.common.domain.entity.reservation.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>, ReservationRepositoryCustom {
    List<Reservation> findByCustomer_UserId(Long UserId);
    List<Reservation> findByManager_UserId(Long UserId);
    List<Reservation> findAllByManager(User manager);

    Page<Reservation> findByReservationStatus(ReservationStatus reservationStatus, Pageable pageable);
    List<Reservation> findByReservationStatusAndManager_UserId(ReservationStatus reservationStatus, Long userId);

    List<Reservation> findByReservationStatusAndManager_UserIdAndReservationDateBetween(ReservationStatus reservationStatus, Long managerId, LocalDate weekStart, LocalDate weekEnd);

    // 수요자가 요청한 예약 시간에 이미 예약이 있는 매니저
    @Query("""
        SELECT distinct r.manager.userId
        FROM Reservation r
        WHERE r.reservationStatus not in ('CANCEL')
            AND r.manager is not null
            AND r.reservationDate = :date
            AND (
                (HOUR(r.reservationTime) * 60 + MINUTE(r.reservationTime)) < :endTime
                AND (HOUR(r.reservationTime) * 60 + MINUTE(r.reservationTime) + r.reservationDuration * 60) > :startTime)
    """)
    List<Long> findBusyManagerIds(
            @Param("date") LocalDate date,
            @Param("startTime") int startTime,
            @Param("endTime") int endTime);

    List<Reservation> findAllByReservationStatus(ReservationStatus status);

    interface ReservationSummaryProjection {
        Long getTotalCount();
        Long getCancelCount();
        Long getCompleteCount();
        Long getUserCount();
    }
    @Query("""
    SELECT
        COUNT(r) AS totalCount,
        SUM(CASE WHEN r.reservationStatus = 'CANCELLED' THEN 1 ELSE 0 END) AS cancelCount,
        SUM(CASE WHEN r.reservationStatus = 'DONE' THEN 1 ELSE 0 END) AS completeCount,
        COUNT(DISTINCT r.customer.userId) AS userCount
    FROM Reservation r
""")
    ReservationSummaryProjection getReservationSummary();

    interface DailyReservationProjection {
        LocalDate getDate();
        Long getDailyReservationsCount();
        Long getDailyCancelCount();
        Long getDailyCompletedCount();
    }
    @Query("""
    SELECT
        r.reservationDate AS date,
        COUNT(r) AS dailyReservationsCount,
        SUM(CASE WHEN r.reservationStatus = 'CANCELLED' THEN 1 ELSE 0 END) AS dailyCancelCount,
        SUM(CASE WHEN r.reservationStatus = 'DONE' THEN 1 ELSE 0 END) AS dailyCompletedCount
    FROM Reservation r
    WHERE r.reservationDate >= :startDate
    GROUP BY r.reservationDate
    ORDER BY r.reservationDate ASC
""")
    List<DailyReservationProjection> getDailyReservations(@Param("startDate") LocalDate startDate);

    interface ReservationCategoryProjection {
        String getCategoryName();
        Long getCategoryCount();
    }
    @Query("""
    SELECT
        r.category.categoryName AS categoryName,
        COUNT(r) AS categoryCount
    FROM Reservation r
    GROUP BY r.category.categoryName
""")
    List<ReservationCategoryProjection> getReservationCountByCategory();
}
