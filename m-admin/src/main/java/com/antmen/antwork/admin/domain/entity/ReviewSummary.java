package com.antmen.antwork.admin.domain.entity;

import com.antmen.antwork.common.domain.entity.User;
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

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "manager_id")
    private User manager;

    @Column(nullable = false)
    @ColumnDefault("0")
    private String totalReviews;

    @Column(nullable = false)
    @ColumnDefault("0.0")
    private float avgRating;
}
