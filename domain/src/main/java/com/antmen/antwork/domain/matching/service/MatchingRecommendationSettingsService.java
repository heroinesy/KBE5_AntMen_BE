package com.antmen.antwork.domain.matching.service;

import com.antmen.antwork.domain.matching.dto.MatchingRecommendationSettingsRequestDto;
import com.antmen.antwork.domain.matching.dto.MatchingRecommendationSettingsResponseDto;
import com.antmen.antwork.domain.matching.entity.MatchingRecommendationSettings;
import com.antmen.antwork.domain.matching.repository.MatchingRecommendationSettingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchingRecommendationSettingsService {

    private final MatchingRecommendationSettingsRepository matchingRecommendationSettingsRepository;
    
    // 기본값 설정 ID를 캐시
    private Long defaultSettingsId = null;

    /**
     * 현재 활성화된 매칭 추천 기준 설정을 조회합니다.
     * 설정이 없으면 기본값을 반환합니다.
     */
    public MatchingRecommendationSettingsResponseDto getCurrentSettings() {
        MatchingRecommendationSettings settings = matchingRecommendationSettingsRepository.findActiveSettings()
                .orElseGet(() -> {
                    log.info("활성화된 매칭 추천 설정이 없어 기본값을 반환합니다.");
                    return getOrCreateDefaultSettings();
                });

        return MatchingRecommendationSettingsResponseDto.from(settings);
    }

    /**
     * 현재 활성화된 매칭 추천 기준 설정 엔티티를 조회합니다.
     * 내부 서비스에서 사용합니다.
     */
    public MatchingRecommendationSettings getCurrentSettingsEntity() {
        return matchingRecommendationSettingsRepository.findActiveSettings()
                .orElseGet(() -> {
                    log.info("활성화된 매칭 추천 설정이 없어 기본값을 반환합니다.");
                    return getOrCreateDefaultSettings();
                });
    }

    /**
     * 매칭 추천 기준 설정을 저장합니다.
     * 기존 활성 설정을 비활성화하고 새로운 설정을 활성화합니다.
     */
    @Transactional
    public MatchingRecommendationSettingsResponseDto saveSettings(MatchingRecommendationSettingsRequestDto requestDto) {
        log.info("매칭 추천 기준 설정을 저장합니다: {}", requestDto);

        // 기존 활성 설정 비활성화
        deactivateCurrentActiveSettings();

        // 새로운 설정 생성 및 저장
        MatchingRecommendationSettings newSettings = MatchingRecommendationSettings.createCustom(
                requestDto.getFirstPriority(),
                requestDto.getSecondPriority(),
                requestDto.getThirdPriority(),
                requestDto.getWorkloadPeriod()
        );

        MatchingRecommendationSettings savedSettings = matchingRecommendationSettingsRepository.save(newSettings);
        log.info("매칭 추천 기준 설정이 저장되었습니다. ID: {}", savedSettings.getId());

        return MatchingRecommendationSettingsResponseDto.from(savedSettings);
    }

    /**
     * 매칭 추천 기준 설정을 기본값으로 초기화합니다.
     */
    @Transactional
    public MatchingRecommendationSettingsResponseDto resetToDefault() {
        log.info("매칭 추천 기준 설정을 기본값으로 초기화합니다.");

        // 기존 활성 설정 비활성화
        deactivateCurrentActiveSettings();

        // 기본값 설정을 활성화
        MatchingRecommendationSettings defaultSettings = getOrCreateDefaultSettings();
        defaultSettings.setActive(true);
        MatchingRecommendationSettings activatedSettings = matchingRecommendationSettingsRepository.save(defaultSettings);
        
        log.info("매칭 추천 기준 설정이 기본값으로 초기화되었습니다. ID: {}", activatedSettings.getId());

        return MatchingRecommendationSettingsResponseDto.from(activatedSettings);
    }

    /**
     * 현재 활성화된 설정을 비활성화합니다.
     */
    @Transactional
    protected void deactivateCurrentActiveSettings() {
        MatchingRecommendationSettings activeSettings = matchingRecommendationSettingsRepository.findActiveSettings()
                .orElse(null);
        
        if (activeSettings != null) {
            activeSettings.setActive(false);
            matchingRecommendationSettingsRepository.save(activeSettings);
            log.debug("활성 설정을 비활성화했습니다. ID: {}", activeSettings.getId());
        }
    }

    /**
     * 기본값 설정을 조회하거나 생성합니다.
     * 기본값은 하나만 존재하며, 없으면 생성합니다.
     */
    @Transactional
    protected MatchingRecommendationSettings getOrCreateDefaultSettings() {
        // 캐시된 ID가 있으면 먼저 시도
        if (defaultSettingsId != null) {
            try {
                MatchingRecommendationSettings cachedSettings = matchingRecommendationSettingsRepository.findById(defaultSettingsId)
                        .orElse(null);
                if (cachedSettings != null && !cachedSettings.isDeleted() && 
                    isDefaultSettings(cachedSettings)) {
                    return cachedSettings;
                }
            } catch (Exception e) {
                log.warn("캐시된 기본값 설정 조회 실패, 새로 조회합니다. ID: {}", defaultSettingsId, e);
            }
        }
        
        // 캐시가 없거나 실패하면 DB에서 조회
        List<MatchingRecommendationSettings> defaultSettingsList = matchingRecommendationSettingsRepository.findDefaultSettings();
        MatchingRecommendationSettings defaultSettings;
        
        if (defaultSettingsList.isEmpty()) {
            log.info("기본값 설정이 없어 새로 생성합니다.");
            defaultSettings = matchingRecommendationSettingsRepository.save(MatchingRecommendationSettings.createDefault());
        } else {
            defaultSettings = defaultSettingsList.get(0); // 첫 번째 기본값 설정 사용
        }
        
        // 캐시 업데이트
        defaultSettingsId = defaultSettings.getId();
        return defaultSettings;
    }
    
    /**
     * 설정이 기본값인지 확인합니다.
     */
    private boolean isDefaultSettings(MatchingRecommendationSettings settings) {
        return "distance".equals(settings.getFirstPriority()) &&
               "review".equals(settings.getSecondPriority()) &&
               "recent".equals(settings.getThirdPriority()) &&
               "1month".equals(settings.getWorkloadPeriod());
    }

    /**
     * 삭제되지 않은 모든 설정 히스토리를 조회합니다.
     */
    public List<MatchingRecommendationSettingsResponseDto> getAllSettingsHistory() {
        List<MatchingRecommendationSettings> allSettings = matchingRecommendationSettingsRepository.findAllNotDeletedOrderByUpdatedAtDesc();
        return allSettings.stream()
                .map(MatchingRecommendationSettingsResponseDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 특정 설정을 활성화합니다.
     */
    @Transactional
    public MatchingRecommendationSettingsResponseDto activateSettings(Long settingsId) {
        MatchingRecommendationSettings settings = matchingRecommendationSettingsRepository.findById(settingsId)
                .orElseThrow(() -> new IllegalArgumentException("설정을 찾을 수 없습니다. ID: " + settingsId));

        log.info("설정을 활성화합니다. ID: {}", settingsId);

        // 기존 활성 설정 비활성화
        deactivateCurrentActiveSettings();

        // 선택된 설정 활성화
        settings.setActive(true);
        MatchingRecommendationSettings activatedSettings = matchingRecommendationSettingsRepository.save(settings);

        log.info("설정이 활성화되었습니다. ID: {}", activatedSettings.getId());

        return MatchingRecommendationSettingsResponseDto.from(activatedSettings);
    }

    /**
     * 특정 설정을 소프트 삭제합니다.
     */
    @Transactional
    public void deleteSettings(Long settingsId) {
        log.info("설정을 소프트 삭제합니다. ID: {}", settingsId);
        
        // 활성 설정은 삭제할 수 없음
        MatchingRecommendationSettings settings = matchingRecommendationSettingsRepository.findById(settingsId)
                .orElseThrow(() -> new IllegalArgumentException("설정을 찾을 수 없습니다. ID: " + settingsId));
        
        if (settings.isActive()) {
            throw new IllegalStateException("활성화된 설정은 삭제할 수 없습니다.");
        }
        
        matchingRecommendationSettingsRepository.softDeleteById(settingsId);
        log.info("설정이 소프트 삭제되었습니다. ID: {}", settingsId);
    }
} 