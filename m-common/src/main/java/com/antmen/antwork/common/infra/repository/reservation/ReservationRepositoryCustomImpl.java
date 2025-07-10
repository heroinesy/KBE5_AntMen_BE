package com.antmen.antwork.common.infra.repository.reservation;

import com.antmen.antwork.common.api.response.reservation.ReservationMatchingListDto;
import com.antmen.antwork.common.domain.entity.account.QUser;
import com.antmen.antwork.common.domain.entity.reservation.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
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

    @Override
    public List<ReservationMatchingListDto> getReservationMatching(String matchingStatus, String searchName, String category, LocalDate reservatedAt) {
        StringBuilder sql = new StringBuilder("""
            SELECT 
                r.reservation_id AS reservationId,
                c.user_id AS customerId,
                c.user_name AS customerName,
                cat.category_name AS categoryName,
                r.reservation_date AS reservationDate,
                r.reservation_time AS reservationTime,
                SUM(CASE WHEN m.matching_is_request = true THEN 1 ELSE 0 END) AS totalRequests,
                SUM(CASE WHEN m.matching_manager_is_accept = true OR m.matching_is_final = true THEN 1 ELSE 0 END) AS totalManagerResponses,
                SUM(CASE WHEN m.matching_manager_is_accept = true THEN 1 ELSE 0 END) AS totalManagerAccepts,
                CASE
                    WHEN SUM(CASE WHEN m.matching_is_request = true THEN 1 ELSE 0 END) = 0 THEN 'nothing'
                    WHEN SUM(CASE WHEN m.matching_manager_is_accept = true OR m.matching_is_final = true THEN 1 ELSE 0 END) =
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
            sql.append(" AND (CAST(c.user_id AS CHAR) = :searchName OR c.user_name = :searchName) ");
        }
        if (category != null && !category.trim().isEmpty()) {
            sql.append(" AND cat.category_name = :category ");
        }
        if (reservatedAt != null) {
            sql.append(" AND r.reservation_date = :reservatedAt ");
        }

        if (matchingStatus != null && !matchingStatus.trim().isEmpty()) {
            sql.append(" AND (CASE " +
                    "WHEN SUM(CASE WHEN m.matching_is_request = true THEN 1 ELSE 0 END) = 0 THEN 'nothing' " +
                    "WHEN SUM(CASE WHEN m.matching_manager_is_accept = true OR m.matching_is_final = true THEN 1 ELSE 0 END) = " +
                    "     SUM(CASE WHEN m.matching_is_request = true THEN 1 ELSE 0 END) THEN 'fail' " +
                    "ELSE 'ing' END) = :matchingStatus ");
        }

        sql.append("""
            GROUP BY r.reservation_id, c.user_id, c.user_name, cat.category_name, r.reservation_date, r.reservation_time
            ORDER BY r.reservation_created_at ASC
        """);

        Query query = em.createNativeQuery(sql.toString(), "ReservationMatchingMapping");

        if (searchName != null && !searchName.trim().isEmpty()) {
            query.setParameter("searchName", searchName);
        }
        if (category != null && !category.trim().isEmpty()) {
            query.setParameter("category", category);
        }
        if (reservatedAt != null) {
            query.setParameter("reservatedAt", reservatedAt);
        }

        return query.getResultList();
    }
}
