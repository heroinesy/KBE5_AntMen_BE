package com.antmen.antwork.common.infra.repository;

import com.antmen.antwork.common.domain.entity.CustomerDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerDetail, Long> {
}