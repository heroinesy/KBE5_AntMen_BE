package com.antmen.antwork.common.infra.repository.account;

import com.antmen.antwork.common.domain.entity.account.User;
import com.antmen.antwork.common.domain.entity.account.UserRole;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserLoginId(String userLoginId);

    // 추후에 조건 추가 예정
    @Query("SELECT u.userId FROM User u WHERE u.userRole = 'MANAGER' " +
    "And ((:reservationId IS NULL) OR " +
    "    (u.userId NOT IN ( " +
            "SELECT m.manager.userId FROM Matching m " +
            "WHERE m.reservation.reservationId = :reservationId))) ")
    List<Long> findTop3AvailableManagers(@Param("reservationId") Long reservationId, Pageable pageable);

    List<User> findByUserRole(UserRole userRole);
}
