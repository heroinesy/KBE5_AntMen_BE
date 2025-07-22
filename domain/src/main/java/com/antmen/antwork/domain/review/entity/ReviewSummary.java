package com.antmen.antwork.domain.review.entity;

import com.antmen.antwork.domain.user.entity.User;
import com.antmen.antwork.domain.user.entity.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "review_summary")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewSummary {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(name = "total_score", nullable = false)
    private Integer totalScore;

    @Column(nullable = false)
    private Long totalReviews;

    @Column(name = "avg_rating", nullable = false, precision = 3, scale = 2)
    private BigDecimal avgRating;

    @Column(name = "updated_at", nullable = false, updatable = false, insertable = false)
    private LocalDateTime updatedAt;
}