package com.antmen.antwork.domain.user.repository;

import com.antmen.antwork.domain.user.entity.ManagerDetail;
import com.antmen.antwork.domain.user.entity.ManagerStatus;
import com.antmen.antwork.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ManagerDetailRepository extends JpaRepository<ManagerDetail, Long> {
    Optional<ManagerDetail> findByUser(User user);

    Optional<ManagerDetail> findByUserId(Long userId);

    List<ManagerDetail> findByUserIdIn(List<Long> userIds);

    List<ManagerDetail> findByManagerStatus(ManagerStatus managerStatus);
}
