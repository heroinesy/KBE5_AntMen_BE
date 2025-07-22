package com.antmen.antwork.domain.user.repository;







@Repository
public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Long> {
    List<CustomerAddress> findByUserUserId(Long userId);
}
