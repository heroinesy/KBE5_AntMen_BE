package com.antmen.antwork.api.admin;

import com.antmen.antwork.domain.matching.dto.MatchingRecommendationSettingsRequestDto;
import com.antmen.antwork.domain.matching.dto.MatchingRecommendationSettingsResponseDto;
import com.antmen.antwork.domain.matching.service.MatchingRecommendationSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/matching-recommendation")
@RequiredArgsConstructor
public class AdminMatchingRecommendationController {

    private final MatchingRecommendationSettingsService matchingRecommendationSettingsService;

    /**
     * 현재 매칭 추천 기준 설정을 조회합니다.
     */
    @GetMapping("/settings")
    public ResponseEntity<MatchingRecommendationSettingsResponseDto> getCurrentSettings() {
        MatchingRecommendationSettingsResponseDto settings = matchingRecommendationSettingsService.getCurrentSettings();
        return ResponseEntity.ok(settings);
    }

    /**
     * 매칭 추천 기준 설정을 저장합니다.
     */
    @PostMapping("/settings")
    public ResponseEntity<MatchingRecommendationSettingsResponseDto> saveSettings(
            @RequestBody MatchingRecommendationSettingsRequestDto requestDto) {
        MatchingRecommendationSettingsResponseDto savedSettings = matchingRecommendationSettingsService.saveSettings(requestDto);
        return ResponseEntity.ok(savedSettings);
    }

    /**
     * 매칭 추천 기준 설정을 기본값으로 초기화합니다.
     */
    @PostMapping("/settings/reset")
    public ResponseEntity<MatchingRecommendationSettingsResponseDto> resetToDefault() {
        MatchingRecommendationSettingsResponseDto defaultSettings = matchingRecommendationSettingsService.resetToDefault();
        return ResponseEntity.ok(defaultSettings);
    }

    /**
     * 모든 설정 히스토리를 조회합니다.
     */
    @GetMapping("/settings/history")
    public ResponseEntity<List<MatchingRecommendationSettingsResponseDto>> getSettingsHistory() {
        List<MatchingRecommendationSettingsResponseDto> history = matchingRecommendationSettingsService.getAllSettingsHistory();
        return ResponseEntity.ok(history);
    }

    /**
     * 특정 설정을 활성화합니다.
     */
    @PostMapping("/settings/{settingsId}/activate")
    public ResponseEntity<MatchingRecommendationSettingsResponseDto> activateSettings(@PathVariable Long settingsId) {
        MatchingRecommendationSettingsResponseDto activatedSettings = matchingRecommendationSettingsService.activateSettings(settingsId);
        return ResponseEntity.ok(activatedSettings);
    }

    /**
     * 특정 설정을 삭제합니다.
     */
    @DeleteMapping("/settings/{settingsId}")
    public ResponseEntity<Void> deleteSettings(@PathVariable Long settingsId) {
        matchingRecommendationSettingsService.deleteSettings(settingsId);
        return ResponseEntity.noContent().build();
    }
} 