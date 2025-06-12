package com.antmen.antwork.common.infra.repository.reservation;

import com.antmen.antwork.common.domain.entity.ReviewSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewSummaryRepository extends JpaRepository<ReviewSummary, Long> {
}
