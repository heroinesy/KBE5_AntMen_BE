package com.antmen.antwork.common.infra.repository.account;

import com.antmen.antwork.common.domain.entity.account.ManagerDetail;
import com.antmen.antwork.common.domain.entity.account.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ManagerDetailRepository extends JpaRepository<ManagerDetail, Long>, ManagerDetailRepositoryCustom {
    Optional<ManagerDetail> findByUser(User user);

    ManagerDetail findByUserId(Long userId);

    @Query("SELECT md FROM ManagerDetail md " +
            "JOIN FETCH md.user u " +
            "WHERE md.managerStatus = 'APPROVED' " +
            "AND (:name IS NULL OR u.userName LIKE %:name%) " +
            "AND (:userId IS NULL OR u.userId = :userId) " +
            "ORDER BY " +
            "CASE WHEN :sortBy = 'joinDate' THEN u.userCreatedAt END DESC, " +
            "CASE WHEN :sortBy = 'lastAccess' THEN u.lastLoginAt END DESC NULLS LAST, " +
            "CASE WHEN :sortBy = 'lastReservation' THEN " +
            "  (SELECT MAX(r.reservationCreatedAt) FROM Reservation r " +
            "   JOIN Matching m ON r.reservationId = m.reservation.reservationId " +
            "   WHERE m.manager = u AND m.matchingIsFinal = true) END DESC NULLS LAST, " +
            "u.userId DESC")
    Page<ManagerDetail> searchApprovedManagersWithPaging(
            @Param("name") String name,
            @Param("userId") Long userId,
            @Param("sortBy") String sortBy,
            Pageable pageable
    );

}
