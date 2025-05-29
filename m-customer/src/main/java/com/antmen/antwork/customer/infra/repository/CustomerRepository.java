package com.antmen.antwork.customer.infra.repository;

import com.antmen.antwork.customer.domain.entity.CustomerDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerDetail, Long> {
}