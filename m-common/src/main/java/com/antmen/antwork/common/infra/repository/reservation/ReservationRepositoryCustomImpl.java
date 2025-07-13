package com.antmen.antwork.common.infra.repository.reservation;

import com.antmen.antwork.common.api.response.reservation.MatchingStatDto;
import com.antmen.antwork.common.api.response.reservation.ReservationAdminListDto;
import com.antmen.antwork.common.api.response.reservation.ReservationMatchingListDto;
import com.antmen.antwork.common.api.response.reservation.ReservationStatDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryCustomImpl implements ReservationRepositoryCustom{
    private final EntityManager em;

    // 수동매칭
    // 표 내용
    @Override
    public List<ReservationMatchingListDto> getReservationMatching(String matchingStatus, String searchName, String category, LocalDate startDate, LocalDate endDate) {
        StringBuilder sql = new StringBuilder("""
            SELECT 
                r.reservation_id AS reservationId,
                c.user_id AS customerId,
                c.user_name AS customerName,
                cat.category_name AS categoryName,
                r.reservation_created_at AS reservationCreatedAt,
                r.reservation_date AS reservationDate,
                r.reservation_time AS reservationTime,
                SUM(CASE WHEN m.matching_is_request = true THEN 1 ELSE 0 END) AS totalRequests,
                SUM(CASE WHEN m.matching_manager_is_accept = true OR m.matching_is_final = true THEN 1 ELSE 0 END) AS totalManagerResponses,
                SUM(CASE WHEN m.matching_manager_is_accept = true THEN 1 ELSE 0 END) AS totalManagerAccepts,
                CASE
                    WHEN SUM(CASE WHEN m.matching_is_request = true THEN 1 ELSE 0 END) = 0 THEN 'nothing'
                    WHEN SUM(CASE WHEN m.matching_manager_is_accept = false OR m.matching_is_final = false THEN 1 ELSE 0 END) =
                         SUM(CASE WHEN m.matching_is_request = true THEN 1 ELSE 0 END) THEN 'fail'
                    ELSE 'ing'
                END AS matchingStatus
            FROM reservation r
            JOIN user c ON r.customer_id = c.user_id
            JOIN category cat ON r.category_id = cat.category_id
            LEFT JOIN matching m ON r.reservation_id = m.reservation_id
            WHERE r.reservation_status = 'WAITING'
        """);

        if (searchName != null && !searchName.trim().isEmpty()) {
            sql.append(" AND (CAST(c.user_id AS CHAR) LIKE CONCAT('%', :searchName, '%') OR c.user_name LIKE CONCAT('%', :searchName, '%')) ");
        }

        if (category != null && !category.trim().isEmpty()) {
            sql.append(" AND cat.category_name LIKE CONCAT('%', :category, '%') ");
        }

        if (startDate != null) {
            sql.append(" AND r.reservation_date >= :startDate ");
        }

        if (endDate != null) {
            sql.append(" AND r.reservation_date <= :endDate ");
        }

        sql.append("""
            GROUP BY r.reservation_id, c.user_id, c.user_name, cat.category_name, r.reservation_date, r.reservation_time
        """);

        if (matchingStatus != null && !matchingStatus.trim().isEmpty()) {
            sql.append("""
                HAVING
                    CASE
                        WHEN SUM(CASE WHEN m.matching_is_request = true THEN 1 ELSE 0 END) = 0 THEN 'nothing'
                        WHEN SUM(CASE WHEN m.matching_manager_is_accept = false OR m.matching_is_final = false THEN 1 ELSE 0 END) =
                             SUM(CASE WHEN m.matching_is_request = true THEN 1 ELSE 0 END) THEN 'fail'
                        ELSE 'ing'
                    END = :matchingStatus
            """);
        }

        sql.append(" ORDER BY r.reservation_created_at ASC ");

        Query query = em.createNativeQuery(sql.toString(), "ReservationMatchingMapping");

        if (searchName != null && !searchName.trim().isEmpty()) {
            query.setParameter("searchName", searchName);
        }
        if (category != null && !category.trim().isEmpty()) {
            query.setParameter("category", category);
        }
        if (startDate != null) {
            query.setParameter("startDate", startDate);
        }
        if (endDate != null) {
            query.setParameter("endDate", endDate);
        }
        if (matchingStatus != null && !matchingStatus.trim().isEmpty()) {
            query.setParameter("matchingStatus", matchingStatus);
        }

        return query.getResultList();
    }

    // 카드 필터 숫자 내용
    @Override
    public List<MatchingStatDto> getMatchingStat(String searchName, String category, LocalDate startDate, LocalDate endDate) {
        StringBuilder sql = new StringBuilder("""
        SELECT sub.status, COUNT(*) AS count
        FROM (
            SELECT
                r.reservation_id,
                CASE
                    WHEN SUM(CASE WHEN m.matching_is_request = true THEN 1 ELSE 0 END) = 0 THEN 'nothing'
                    WHEN SUM(CASE WHEN m.matching_manager_is_accept = false OR m.matching_is_final = false THEN 1 ELSE 0 END) =
                         SUM(CASE WHEN m.matching_is_request = true THEN 1 ELSE 0 END) THEN 'fail'
                    ELSE 'ing'
                END AS status
            FROM reservation r
            JOIN user c ON r.customer_id = c.user_id
            JOIN category cat ON r.category_id = cat.category_id
            LEFT JOIN matching m ON r.reservation_id = m.reservation_id
            WHERE r.reservation_status = 'WAITING'
    """);

        if (searchName != null && !searchName.trim().isEmpty()) {
            sql.append(" AND (CAST(c.user_id AS CHAR) LIKE CONCAT('%', :searchName, '%') OR c.user_name LIKE CONCAT('%', :searchName, '%')) ");
        }
        if (category != null && !category.trim().isEmpty()) {
            sql.append(" AND cat.category_name LIKE CONCAT('%', :category, '%') ");
        }
        if (startDate != null) {
            sql.append(" AND r.reservation_date >= :startDate ");
        }
        if (endDate != null) {
            sql.append(" AND r.reservation_date <= :endDate ");
        }

        sql.append("""
            GROUP BY r.reservation_id
        ) AS sub
        GROUP BY sub.status
    """);

        Query query = em.createNativeQuery(sql.toString(), "MatchingStatMapping");

        if (searchName != null && !searchName.trim().isEmpty()) {
            query.setParameter("searchName", searchName);
        }
        if (category != null && !category.trim().isEmpty()) {
            query.setParameter("category", category);
        }
        if (startDate != null) {
            query.setParameter("startDate", startDate);
        }
        if (endDate != null) {
            query.setParameter("endDate", endDate);
        }

        return query.getResultList();
    }

    // 예약 현황
    // 카드 필터 숫자 내용
    @Override
    public List<ReservationStatDto> getCountOfReservationStatus(String searchName, String category, LocalDate startDate, LocalDate endDate) {
        StringBuilder sql = new StringBuilder("""
                SELECT r.reservation_status AS status, COUNT(*) AS count
                FROM reservation r
                LEFT JOIN user u ON r.customer_id = u.user_id
                LEFT JOIN category cat ON r.category_id = cat.category_id
                WHERE 1 = 1
                """);

        if (searchName != null && !searchName.trim().isEmpty()) {
            sql.append(" AND (CAST(u.user_id AS CHAR) LIKE CONCAT('%', :searchName, '%') OR u.user_name LIKE CONCAT('%', :searchName, '%')) ");
        }
        if (category != null && !category.trim().isEmpty()) {
            sql.append(" AND cat.category_name LIKE CONCAT('%', :category, '%') ");
        }
        if (startDate != null) {
            sql.append(" AND r.reservation_date >= :startDate ");
        }
        if (endDate != null) {
            sql.append(" AND r.reservation_date <= :endDate ");
        }

        sql.append("GROUP BY r.reservation_status");

        Query query = em.createNativeQuery(sql.toString(), "ReservationStatMapping");

        if (searchName != null && !searchName.trim().isEmpty()) {
            query.setParameter("searchName", searchName);
        }
        if (category != null && !category.trim().isEmpty()) {
            query.setParameter("category", category);
        }
        if (startDate != null) {
            query.setParameter("startDate", startDate);
        }
        if (endDate != null) {
            query.setParameter("endDate", endDate);
        }

        return query.getResultList();
    }

    // 표 내용
    @Override
    public List<ReservationAdminListDto> getReservationAdminList(String reservationStatus, String searchName, String category, LocalDate startDate, LocalDate endDate) {
        StringBuilder sql = new StringBuilder("""
                SELECT r.reservation_id,
                    r.customer_id,
                    c.user_name AS customerName,
                    cat.category_name,
                    r.reservation_status,
                    r.reservation_created_at,
                    r.reservation_date,
                    r.reservation_time
                FROM reservation r
                Left JOIN user c ON r.customer_id = c.user_id
                Left JOIN category cat ON r.category_id = cat.category_id
                WHERE 1 = 1
                """);

        if (reservationStatus != null && !reservationStatus.trim().isEmpty()) {
            sql.append(" AND r.reservation_status = :reservationStatus ");
        }
        if (searchName != null && !searchName.trim().isEmpty()) {
            sql.append(" AND (CAST(c.user_id AS CHAR) LIKE CONCAT('%', :searchName, '%') OR c.user_name LIKE CONCAT('%', :searchName, '%')) ");
        }
        if (category != null && !category.trim().isEmpty()) {
            sql.append(" AND cat.category_name LIKE CONCAT('%', :category, '%') ");
        }
        if (startDate != null) {
            sql.append(" AND r.reservation_date >= :startDate ");
        }
        if (endDate != null) {
            sql.append(" AND r.reservation_date <= :endDate ");
        }

        sql.append("ORDER BY r.reservation_created_at DESC");

        Query query = em.createNativeQuery(sql.toString(), "ReservationAdminListMapping");
        if (reservationStatus != null && !reservationStatus.trim().isEmpty()) {
            query.setParameter("reservationStatus", reservationStatus);
        }
        if (searchName != null && !searchName.trim().isEmpty()) {
            query.setParameter("searchName", searchName);
        }
        if (category != null && !category.trim().isEmpty()) {
            query.setParameter("category", category);
        }
        if (startDate != null) {
            query.setParameter("startDate", startDate);
        }
        if (endDate != null) {
            query.setParameter("endDate", endDate);
        }

        return query.getResultList();
    }

}
