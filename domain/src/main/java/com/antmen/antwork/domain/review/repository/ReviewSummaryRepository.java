package com.antmen.antwork.domain.review.repository;

import com.antmen.antwork.domain.review.entity.ReviewSummary;
import com.antmen.antwork.domain.user.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewSummaryRepository extends JpaRepository<ReviewSummary, Long> {
    Optional<ReviewSummary> findByUserIdAndRole(Long userId, UserRole role);

    /**
     * 여러 매니저의 리뷰 요약 정보를 한 번에 조회
     */
    @Query("SELECT rs FROM ReviewSummary rs WHERE rs.userId IN :userIds AND rs.role = :role")
    List<ReviewSummary> findByUserIdInAndRole(@Param("userIds") List<Long> userIds, @Param("role") UserRole role);

    interface ReviewerSatisfactionProjection {
        Long getUserId();
        String getName();
        BigDecimal getAvgRating();
        Long getTotalReviews();
    }
    @Query("""
        SELECT rs.userId AS userId,
               u.userName AS name,
               rs.avgRating AS avgRating,
               rs.totalReviews AS totalReviews
        FROM ReviewSummary rs
        JOIN User u ON rs.userId = u.userId
        WHERE rs.role = :role
        ORDER BY rs.avgRating DESC, rs.totalReviews DESC
    """)
    List<ReviewerSatisfactionProjection> findTopByAvgRating(@Param("role") UserRole role, Pageable pageable);

    // 2. 리뷰 개수 많은 순 Projection (Top N)
    @Query("""
        SELECT rs.userId AS userId,
               u.userName AS name,
               rs.avgRating AS avgRating,
               rs.totalReviews AS totalReviews
        FROM ReviewSummary rs
        JOIN User u ON rs.userId = u.userId
        WHERE rs.role = :role
        ORDER BY rs.totalReviews DESC
    """)
    List<ReviewerSatisfactionProjection> findTopByTotalReviews(@Param("role") UserRole role, Pageable pageable);
}