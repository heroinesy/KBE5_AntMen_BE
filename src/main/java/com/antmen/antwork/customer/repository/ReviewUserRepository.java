package com.antmen.antwork.customer.repository;

import com.antmen.antwork.entity.ReviewUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewUserRepository extends JpaRepository<ReviewUser, Integer> {
}
