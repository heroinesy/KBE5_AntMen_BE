package com.antmen.antwork.domain.user.repository;








@Repository
public interface CustomerDetailRepository extends JpaRepository<CustomerDetail, Long> {
    Optional<CustomerDetail> findByUser(User user);
}