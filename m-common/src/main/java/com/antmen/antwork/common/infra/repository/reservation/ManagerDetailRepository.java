package com.antmen.antwork.common.infra.repository.account;

import com.antmen.antwork.common.domain.entity.account.ManagerDetail;
import com.antmen.antwork.common.domain.entity.account.ManagerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagerDetailRepository extends JpaRepository<ManagerDetail, Long> {
    List<ManagerDetail> findByManagerStatus(ManagerStatus managerStatus);
    List<ManagerDetail> findByUserIdIn(List<Long> userIds);
} 