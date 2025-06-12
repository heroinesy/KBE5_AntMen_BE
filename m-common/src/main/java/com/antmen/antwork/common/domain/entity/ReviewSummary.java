package com.antmen.antwork.common.domain.entity;

import com.antmen.antwork.common.domain.entity.account.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "review_summary")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewSummary {
    @Id
    private Long managerId;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Long totalReviews;

    @Column(nullable = false)
    @ColumnDefault("0.0")
    private float avgRating;
}

