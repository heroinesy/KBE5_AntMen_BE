package com.antmen.antwork.manager.repository;

import com.antmen.antwork.entity.ReviewMate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewMateRepo extends JpaRepository<ReviewMate,Integer> {
}
