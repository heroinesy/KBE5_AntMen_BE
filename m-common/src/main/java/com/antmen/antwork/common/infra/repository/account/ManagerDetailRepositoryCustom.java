package com.antmen.antwork.common.infra.repository.account;

import com.antmen.antwork.common.domain.entity.account.ManagerDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ManagerDetailRepositoryCustom {
    Page<ManagerDetail> findByManagerStatusIsWaitingOrReapplyWithName(String name, Pageable pageable);
    Page<ManagerDetail> searchApprovedManagersWithPaging(String name, String sortBy, Pageable pageable);
}
