package com.antmen.antwork.common.infra.repository.account;

import com.antmen.antwork.common.domain.entity.account.CustomerDetail;
import com.antmen.antwork.common.domain.entity.account.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerDetailRepository extends JpaRepository<CustomerDetail, Long> {
    Optional<CustomerDetail> findByUser(User user);
}