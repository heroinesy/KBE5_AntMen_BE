package com.antmen.antwork.common.api.response.reservation;

import com.antmen.antwork.common.domain.entity.reservation.MatchingRecommendationSettings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchingRecommendationSettingsResponseDto {

    private Long id;
    private String firstPriority;
    private String secondPriority;
    private String thirdPriority;
    private String workloadPeriod;
    private boolean isActive;
    private LocalDateTime updatedAt;

    public static MatchingRecommendationSettingsResponseDto from(MatchingRecommendationSettings settings) {
        return new MatchingRecommendationSettingsResponseDto(
                settings.getId(),
                settings.getFirstPriority(),
                settings.getSecondPriority(),
                settings.getThirdPriority(),
                settings.getWorkloadPeriod(),
                settings.isActive(),
                settings.getUpdatedAt()
        );
    }
} 