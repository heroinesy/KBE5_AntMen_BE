package com.antmen.antwork.common.infra.repository.reservation;

import com.antmen.antwork.common.domain.entity.ReviewSummary;
import com.antmen.antwork.common.domain.entity.account.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewSummaryRepository extends JpaRepository<ReviewSummary, Long> {
    Optional<ReviewSummary> findByUserIdAndRole(Long userId, UserRole role);

    interface ReviewerSatisfactionProjection {
        Long getUserId();
        String getName();
        BigDecimal getAvgRating();
        Long getTotalReviews();
    }
    @Query("SELECT rs FROM ReviewSummary rs " +
            "WHERE rs.role = :role " +
            "ORDER BY rs.avgRating DESC, rs.totalReviews DESC ")
    List<ReviewSummary> findTopByRole(UserRole role, Pageable pageable);
}