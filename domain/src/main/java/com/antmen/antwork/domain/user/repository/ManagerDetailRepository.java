package com.antmen.antwork.domain.user.repository;










@Repository
public interface ManagerDetailRepository extends JpaRepository<ManagerDetail, Long>, ManagerDetailRepositoryCustom {
    Optional<ManagerDetail> findByUser(User user);

    Optional<ManagerDetail> findByUserId(Long userId);

    List<ManagerDetail> findByUserIdIn(List<Long> userIds);

    List<ManagerDetail> findByManagerStatus(ManagerStatus managerStatus);
}
