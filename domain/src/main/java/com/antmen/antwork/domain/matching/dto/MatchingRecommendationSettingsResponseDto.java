package com.antmen.antwork.domain.matching.dto;

import com.antmen.antwork.domain.matching.entity.MatchingRecommendationSettings;
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