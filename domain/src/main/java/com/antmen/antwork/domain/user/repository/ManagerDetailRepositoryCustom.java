package com.antmen.antwork.domain.user.repository;





public interface ManagerDetailRepositoryCustom {
    Page<ManagerDetail> findByManagerStatusIsWaitingOrReapplyWithName(String name, Pageable pageable);
    Page<ManagerDetail> searchApprovedManagersWithPaging(String name, String sortBy, Pageable pageable);
}
