package com.antmen.antwork.domain.matching.repository;











@Repository
public interface MatchingRecommendationSettingsRepository extends JpaRepository<MatchingRecommendationSettings, Long> {

    /**
     * 현재 활성화된 설정을 조회합니다.
     */
    @Query("SELECT m FROM MatchingRecommendationSettings m WHERE m.isActive = true AND m.isDeleted = false ORDER BY m.updatedAt DESC")
    Optional<MatchingRecommendationSettings> findActiveSettings();



    /**
     * 삭제되지 않은 모든 설정을 수정일시 역순으로 조회합니다.
     */
    @Query("SELECT m FROM MatchingRecommendationSettings m WHERE m.isDeleted = false ORDER BY m.updatedAt DESC")
    List<MatchingRecommendationSettings> findAllNotDeletedOrderByUpdatedAtDesc();

    /**
     * 특정 설정을 소프트 삭제합니다.
     */
    @Modifying
    @Query("UPDATE MatchingRecommendationSettings m SET m.isDeleted = true WHERE m.id = :id")
    void softDeleteById(@Param("id") Long id);

    /**
     * 기본값 설정을 조회합니다.
     * 기본값은 distance, review, recent, 1month 조합으로 식별합니다.
     * 캐시를 우선 사용하고, 필요시에만 호출됩니다.
     */
    @Query("SELECT m FROM MatchingRecommendationSettings m WHERE m.firstPriority = 'distance' AND m.secondPriority = 'review' AND m.thirdPriority = 'recent' AND m.workloadPeriod = '1month' AND m.isDeleted = false")
    List<MatchingRecommendationSettings> findDefaultSettings();

    /**
     * 특정 ID의 설정을 조회합니다.
     */
    Optional<MatchingRecommendationSettings> findById(Long id);
} 