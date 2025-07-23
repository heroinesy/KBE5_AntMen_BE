package com.antmen.antwork.common.domain.entity.reservation;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "matching_recommendation_settings", indexes = {
    @Index(name = "idx_active_settings", columnList = "is_active,is_deleted"),
    @Index(name = "idx_updated_at", columnList = "updated_at")
})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class MatchingRecommendationSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_priority", nullable = false)
    private String firstPriority; // distance, review, recent, workload, review_count

    @Column(name = "second_priority", nullable = false)
    private String secondPriority;

    @Column(name = "third_priority", nullable = false)
    private String thirdPriority;

    @Column(name = "workload_period", nullable = false)
    private String workloadPeriod; // 1week, 2week, 1month, 3month, 6month

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // 기본값으로 생성하는 정적 팩토리 메서드
    public static MatchingRecommendationSettings createDefault() {
        MatchingRecommendationSettings settings = new MatchingRecommendationSettings();
        settings.firstPriority = "distance";
        settings.secondPriority = "review";
        settings.thirdPriority = "recent";
        settings.workloadPeriod = "1month";
        settings.isActive = true;
        return settings;
    }

    // 사용자 정의 설정으로 생성하는 정적 팩토리 메서드
    public static MatchingRecommendationSettings createCustom(String firstPriority, String secondPriority, String thirdPriority, String workloadPeriod) {
        MatchingRecommendationSettings settings = new MatchingRecommendationSettings();
        settings.firstPriority = firstPriority;
        settings.secondPriority = secondPriority;
        settings.thirdPriority = thirdPriority;
        settings.workloadPeriod = workloadPeriod;
        settings.isActive = true;
        return settings;
    }

    // 설정 업데이트 메서드
    public void updateSettings(String firstPriority, String secondPriority, String thirdPriority, String workloadPeriod) {
        this.firstPriority = firstPriority;
        this.secondPriority = secondPriority;
        this.thirdPriority = thirdPriority;
        this.workloadPeriod = workloadPeriod;
    }
} 