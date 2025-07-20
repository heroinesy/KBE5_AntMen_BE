package com.antmen.antwork.domain.user.repository;

import com.antmen.antwork.common.domain.entity.account.ManagerDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ManagerDetailRepositoryCustom {
    Page<ManagerDetail> findByManagerStatusIsWaitingOrReapplyWithName(String name, Pageable pageable);
    Page<ManagerDetail> searchApprovedManagersWithPaging(String name, String sortBy, Pageable pageable);
}
