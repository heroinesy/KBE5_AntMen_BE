package com.antmen.antwork.common.infra.repository;

import com.antmen.antwork.common.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserLoginId(String userLoginId);

    // 추후에 조건 추가 예정
    @Query("SELECT u.userId FROM User u WHERE u.userRole = 'MANAGER'")
    List<Long> findTop3AvailableManagers(Pageable pageable);
}
