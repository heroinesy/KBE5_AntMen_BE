package com.antmen.antwork.domain.user.repository;


import com.antmen.antwork.domain.user.entity.CustomerDetail;
import com.antmen.antwork.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerDetailRepository extends JpaRepository<CustomerDetail, Long> {
    Optional<CustomerDetail> findByUser(User user);
}