package com.antmen.antwork.common.infra.repository.account;

import com.antmen.antwork.common.domain.entity.account.CustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Long> {
    List<CustomerAddress> findByUserUserId(Long userId);
}
