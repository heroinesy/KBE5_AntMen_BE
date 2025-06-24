package com.antmen.antwork.common.infra.repository.account;

import com.antmen.antwork.common.domain.entity.account.ManagerDetail;

import java.util.List;

public interface ManagerDetailRepositoryCustom {
    List<ManagerDetail> findByManagerStatusIsWaitingOrReapply();
}
