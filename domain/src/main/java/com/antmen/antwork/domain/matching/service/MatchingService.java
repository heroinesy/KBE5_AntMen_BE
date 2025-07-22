package com.antmen.antwork.domain.matching.service;

































@Slf4j
@Service
@RequiredArgsConstructor
public class MatchingService {
    private final MatchingRepository matchingRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final AlertService alertService;
    private final ManagerDetailRepository managerDetailRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final MatchingRecommendationSettingsService matchingRecommendationSettingsService;
    private final ReviewRepository reviewRepository;
    private final ReviewSummaryRepository reviewSummaryRepository;

    // 매칭 생성
    @Transactional
    public void initiateMatching(Long reservationId, List<Long> managerIds) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약이 존재하지 않습니다."));

        MatchingRequestDto matchingRequestDto = MatchingRequestDto.builder()
                .reservationId(reservation.getReservationId())
                .addressId(reservation.getAddress().getAddressId())
                .reservationDate(reservation.getReservationDate())
                .reservationTime(reservation.getReservationTime())
                .reservationDuration(reservation.getReservationDuration())
                .build();

        // 자동추천 - 매칭 추천 기준 설정 적용
        List<Long> selectedManagerIds = (managerIds == null || managerIds.isEmpty())
                ? selectTop3Candidate(matchingRequestDto, "custom", true, false).stream()
                .map(MatchingManagerListResponseDto::getManagerId)
                .toList()
                : managerIds;

        List<Matching> matchingList = new ArrayList<>();
        int basePriority = 1;


        for (Long managerId : selectedManagerIds) {
            User manager = userRepository.findById(managerId)
                    .orElseThrow(() -> new IllegalArgumentException("매니저가 없습니다."));
            Matching matching = Matching.builder()
                    .reservation(reservation)
                    .manager(manager)
                    .matchingPriority(basePriority++)
                    .matchingIsRequest(false)
                    .matchingUpdatedAt(LocalDateTime.now())
                    .build();
            matchingList.add(matching);
        }
        matchingRepository.saveAll(matchingList);

        // 1순위에게 알림 전송
        if (!matchingList.isEmpty()) {
            Matching top = matchingList.get(0); // 우선 순위대로 추가했으므로 첫 번째가 최우선
            top.setMatchingIsRequest(true);
            top.setMatchingUpdatedAt(LocalDateTime.now());

//            alertService.sendAlert(top.getManager().getUserId(), AlertTrigger.MATCHING_REQUEST_TO_MANAGER,top.getReservation().getReservationId(), null);

        }
    }

    // 다음 매칭 요청
    @Transactional
    public void triggerNextMatching(Matching rejectedMatching) {
        Reservation reservation = rejectedMatching.getReservation();
        Long reservationId = reservation.getReservationId();
        int currentPriority = rejectedMatching.getMatchingPriority();

        // 다음 매칭 후보 찾기
        Optional<Matching> optionalNext = matchingRepository
                .findTopByReservation_ReservationIdAndMatchingPriorityGreaterThanOrderByMatchingPriorityAsc(
                        reservationId, currentPriority);

        if (optionalNext.isPresent()) {
            Matching nextMatching = optionalNext.get();

            if (!Boolean.TRUE.equals(nextMatching.getMatchingIsRequest())) {
                nextMatching.setMatchingIsRequest(true);
                nextMatching.setMatchingUpdatedAt(LocalDateTime.now());

                alertService.sendAlert(nextMatching.getManager().getUserId(), AlertTrigger.MATCHING_REQUEST_TO_MANAGER,nextMatching.getReservation().getReservationId());


                log.info("➡️ 다음 매니저에게 매칭 요청 전송: reservationId={}, matchingId={}, priority={}",
                        reservationId, nextMatching.getMatchingId(), nextMatching.getMatchingPriority());
            } else {
                log.info("❗ 다음 매니저는 이미 요청됨 → 무시: reservationId={}, priority={}", reservationId, nextMatching.getMatchingPriority());
            }

            return;
        }

        // 다음 매칭이 없으면 새 매칭 생성 (재추천)
        log.info("🔁 새로운 매칭 생성: reservationId={}, fromPriority={}", reservationId, currentPriority + 1);

        MatchingRequestDto dto = MatchingRequestDto.builder()
                .reservationId(reservationId)
                .addressId(reservation.getAddress().getAddressId())
                .reservationDate(reservation.getReservationDate())
                .reservationTime(reservation.getReservationTime())
                .reservationDuration(reservation.getReservationDuration())
                .build();

        List<Long> recommendedIds = selectTop3Candidate(dto, "custom", true, true)
                .stream()
                .map(MatchingManagerListResponseDto::getManagerId)
                .toList();

        int newPriority = currentPriority + 1;
        for (Long managerId : recommendedIds) {
            User manager = userRepository.findById(managerId)
                    .orElseThrow(() -> new NotFoundException("매니저를 찾을 수 없습니다."));

            Matching newMatching = Matching.builder()
                    .reservation(reservation)
                    .manager(manager)
                    .matchingPriority(newPriority++)
                    .matchingIsRequest(false)
                    .matchingUpdatedAt(LocalDateTime.now())
                    .build();

            matchingRepository.save(newMatching);
            log.info("➕ 새로운 매칭 후보 추가: managerId={}, priority={}", managerId, newMatching.getMatchingPriority());
        }

        // 첫 번째 후보에게 즉시 요청
        matchingRepository.findTopByReservation_ReservationIdAndMatchingPriorityGreaterThanOrderByMatchingPriorityAsc(
                        reservationId, currentPriority)
                .ifPresent(first -> {
                    first.setMatchingIsRequest(true);
                    first.setMatchingUpdatedAt(LocalDateTime.now());

                    alertService.sendAlert(first.getManager().getUserId(), AlertTrigger.MATCHING_REQUEST_TO_MANAGER,first.getReservation().getReservationId());

                    log.info("➡️ 재추천된 첫 매니저에게 요청 전송: reservationId={}, managerId={}", reservationId, first.getManager().getUserId());
                });
    }

    // 매니저 매칭 답장
    @Transactional
    public void managerRespondMatching(Long matchingId, MatchingManagerRequestDto matchingManagerRequestDto) {
        Matching matching = matchingRepository.findById(matchingId)
                .orElseThrow(() -> new IllegalArgumentException("매칭 정보를 찾을 수 없습니다."));
        if (matching.getMatchingManagerIsAccept() != null) {
            throw new IllegalStateException("이미 응답한 매칭입니다.");}

        boolean isAccept = matchingManagerRequestDto.getMatchingManagerIsAccept();
        matching.setMatchingManagerIsAccept(isAccept);
        matching.setMatchingUpdatedAt(LocalDateTime.now());

        // 수락시 수요자에게 알림
        if (isAccept) {
            alertService.sendAlert(matching.getReservation().getCustomer().getUserId(), AlertTrigger.MATCHING_ACCEPTED_BY_MANAGER,matching.getReservation().getReservationId());

        } else {
            if (matchingManagerRequestDto.getMatchingRefuseReason() == null || matchingManagerRequestDto.getMatchingRefuseReason().isBlank()) {
                throw new IllegalStateException("매칭 거절 사유는 필수입니다.");}
            matching.setMatchingRefuseReason(matchingManagerRequestDto.getMatchingRefuseReason());
            matching.setMatchingIsFinal(false);
            // 거절시 다음 순위로 넘어감
            triggerNextMatching(matching);
        }
    }

    // 수요자 매칭 답장
    @Transactional
    public void customerResponseMatching(Long matchingId, MatchingResponseRequestDto requestDto) {
        Matching matching = matchingRepository.findById(matchingId)
                .orElseThrow(() -> new NotFoundException("매칭 정보를 찾을 수 없습니다."));

        if (matching.getMatchingIsFinal() != null) {
            throw new IllegalStateException("이미 응답한 매칭입니다.");
        }

        // 매니저가 거절했으면 수요자 수락 불가
        if (Boolean.FALSE.equals(matching.getMatchingManagerIsAccept())) {
            if (!requestDto.getMatchingIsFinal()
                    && (requestDto.getMatchingRefuseReason() == null || requestDto.getMatchingRefuseReason().isBlank())) {
                throw new IllegalArgumentException("매칭 거절 사유는 필수입니다.");
            }
            matching.setMatchingIsFinal(false);
            matching.setMatchingUpdatedAt(LocalDateTime.now());
            triggerNextMatching(matching);
            return;
        }

        matching.setMatchingIsFinal(requestDto.getMatchingIsFinal());
        matching.setMatchingUpdatedAt(LocalDateTime.now());

        if (!requestDto.getMatchingIsFinal()) {
            if (requestDto.getMatchingRefuseReason() != null) {
                matching.setMatchingRefuseReason(requestDto.getMatchingRefuseReason());
            }

            Reservation reservation = matching.getReservation();
            boolean isLastPriority = matchingRepository
                    .findTopByReservation_ReservationIdAndMatchingPriorityGreaterThanOrderByMatchingPriorityAsc(
                            reservation.getReservationId(), matching.getMatchingPriority()
                    )
                    .isEmpty();

            matching.setMatchingIsFinal(false);

            if (isLastPriority) {
                log.info("♻️ 마지막 매니저 수요자 거절 → 재매칭 실행: reservationId={}", reservation.getReservationId());
                triggerNextMatching(matching);
            } else {
                log.info("⏭ 수요자 거절 → 다음 순위 매니저에게 바로 요청: reservationId={}", reservation.getReservationId());
                triggerNextMatching(matching);
            }
            return;
        }

        // 수락 시
        Reservation reservation = matching.getReservation();
        reservation.setReservationStatus(ReservationStatus.MATCHING);
        reservation.setManager(matching.getManager());
        reservation.setMatchedAt(LocalDateTime.now());

        alertService.sendAlert(reservation.getManager().getUserId(), AlertTrigger.MATCHING_CONFIRMED_BY_CUSTOMER,reservation.getReservationId());

        List<Matching> otherMatchings = matchingRepository
                .findAllByReservation_ReservationId(reservation.getReservationId());

        for (Matching m : otherMatchings) {
            if (!m.getMatchingId().equals(matchingId) && Boolean.TRUE.equals(m.getMatchingIsRequest())) {
                alertService.sendAlert(m.getManager().getUserId(), AlertTrigger.MATCHING_LOST_TO_MANAGER,m.getReservation().getReservationId());

            }
        }
    }

    // 매칭 신청 가능한 매니저 리스트 조회 (시간)
    @Transactional(readOnly = true)
    public List<MatchingManagerListResponseDto> getManagerList(MatchingRequestDto requestDto, boolean useDistanceFilter, String sortType) {
        List<MatchingManagerListResponseDto> filteredManager = getFilteredManagers(
                requestDto.getReservationDate(),
                requestDto.getReservationTime(),
                requestDto.getReservationDuration(),
                requestDto.getAddressId(),
                useDistanceFilter,
                requestDto.getReservationId(),
                false
        );
        return sortManagerDtos(filteredManager,sortType);
    }

    // 자동추천 3명
    @Transactional
    public List<MatchingManagerListResponseDto> selectTop3Candidate(MatchingRequestDto requestDto, String sortType, boolean useDistanceFilter, boolean excludeMatchedManagers) {
        List<MatchingManagerListResponseDto> filteredManager = getFilteredManagers(
                requestDto.getReservationDate(),
                requestDto.getReservationTime(),
                requestDto.getReservationDuration(),
                requestDto.getAddressId(),
                true, // useTimeFilter
                requestDto.getReservationId(),
                excludeMatchedManagers
        );
        return sortManagerDtos(filteredManager,sortType).stream().limit(3).toList();
    }

    /**
     * 매칭 유틸 메소드
     * 1. 위경도 계산 calculateDistance
     * 2. 정렬 (리뷰, 최근 가입순, 거리순) sortManagerDtos
     * 3. 매칭 추천 (시간, 거리 적용) getFilteredManagers
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS_KM = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double rLat1 = Math.toRadians(lat1);
        double rLat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(rLat1) * Math.cos(rLat2)
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

    private List<MatchingManagerListResponseDto> sortManagerDtos(List<MatchingManagerListResponseDto> dtos, String sortType) {
        // 매칭 추천 기준 설정 조회
        MatchingRecommendationSettings settings = null;
        try {
            settings = matchingRecommendationSettingsService.getCurrentSettingsEntity();
            log.info("매칭 추천 기준 설정 조회 성공: settingsId={}, isActive={}, firstPriority={}, secondPriority={}, thirdPriority={}", 
                    settings.getId(), settings.isActive(), settings.getFirstPriority(), settings.getSecondPriority(), settings.getThirdPriority());
        } catch (Exception e) {
            log.warn("매칭 추천 기준 설정 조회 실패, 기본 정렬 사용: {}", e.getMessage());
        }
        
        // 기존 정렬 로직 (fallback)
        return switch (sortType.toLowerCase()) {
            case "custom" -> {
                if (settings != null && settings.isActive()) {
                    log.info("매칭 추천 기준 설정 적용: {}개 매니저 정렬", dtos.size());
                    yield sortByRecommendationSettings(dtos, settings);
                } else {
                    log.info("매칭 추천 기준 설정 미적용 (설정 없음 또는 비활성), 기본 거리순 정렬 사용");
                    yield dtos.stream()
                            .sorted(Comparator.comparingDouble(dto -> Optional.ofNullable(dto.getDistance()).orElse(Double.MAX_VALUE)))
                            .toList();
                }
            }
//             todo: 리뷰 기반 정렬은 reviewSummary 기능 구현 후 활성화 진행할게용
            case "review" -> dtos.stream()
                    .sorted(Comparator.comparingDouble(MatchingManagerListResponseDto::getManagerRating).reversed())
                    .toList();
            case "recent" -> dtos.stream()
                    .sorted(Comparator.comparing(MatchingManagerListResponseDto::getManagerId).reversed())
                    .toList();
            case "distance" -> dtos.stream()
                    .sorted(Comparator.comparingDouble(dto -> Optional.ofNullable(dto.getDistance()).orElse(Double.MAX_VALUE)))
                    .toList();
            default -> dtos;
        };
    }

    /**
     * 매칭 추천 기준 설정에 따른 정렬 (최적화 버전)
     */
    private List<MatchingManagerListResponseDto> sortByRecommendationSettings(
            List<MatchingManagerListResponseDto> dtos, 
            MatchingRecommendationSettings settings) {
        
        log.info("매칭 추천 기준 설정 정렬 시작: 1순위={}, 2순위={}, 3순위={}, 매니저 수={}", 
                settings.getFirstPriority(), settings.getSecondPriority(), settings.getThirdPriority(), dtos.size());
        
        // 매니저 ID 목록 추출
        List<Long> managerIds = dtos.stream()
                .map(MatchingManagerListResponseDto::getManagerId)
                .toList();
        
        // 한 번에 모든 데이터 조회 (최적화)
        Map<Long, Double> workloadMap = getWorkloadForAllManagers(managerIds, settings.getWorkloadPeriod());
        Map<Long, Integer> reviewCountMap = getReviewCountForAllManagers(managerIds);
        
        log.info("정렬 데이터 준비 완료: 근무량 데이터={}개, 리뷰 수 데이터={}개", workloadMap.size(), reviewCountMap.size());
        
        // 각 매니저의 정렬 기준별 값들을 로그로 출력
        log.info("=== 정렬 전 매니저 데이터 ===");
        for (MatchingManagerListResponseDto dto : dtos) {
            Long managerId = dto.getManagerId();
            log.info("매니저 {}: 거리={}, 평점={}, 근무량={}, 리뷰수={}", 
                    managerId,
                    dto.getDistance(),
                    dto.getManagerRating(),
                    workloadMap.getOrDefault(managerId, 0.0),
                    reviewCountMap.getOrDefault(managerId, 0));
        }
        
        List<MatchingManagerListResponseDto> sorted = dtos.stream()
                .sorted((dto1, dto2) -> {
                    // 디버깅 로그 추가
                    log.debug("정렬 비교: manager1={}, rating1={}, manager2={}, rating2={}", 
                            dto1.getManagerId(), dto1.getManagerRating(), 
                            dto2.getManagerId(), dto2.getManagerRating());
                    
                    // 1순위 비교
                    int firstCompare = compareByPriorityOptimized(dto1, dto2, settings.getFirstPriority(), workloadMap, reviewCountMap);
                    if (firstCompare != 0) {
                        log.debug("1순위 비교 결과: {} (기준: {})", firstCompare, settings.getFirstPriority());
                        return firstCompare;
                    }
                    
                    // 2순위 비교
                    int secondCompare = compareByPriorityOptimized(dto1, dto2, settings.getSecondPriority(), workloadMap, reviewCountMap);
                    if (secondCompare != 0) {
                        log.debug("2순위 비교 결과: {} (기준: {})", secondCompare, settings.getSecondPriority());
                        return secondCompare;
                    }
                    
                    // 3순위 비교
                    int thirdCompare = compareByPriorityOptimized(dto1, dto2, settings.getThirdPriority(), workloadMap, reviewCountMap);
                    log.debug("3순위 비교 결과: {} (기준: {})", thirdCompare, settings.getThirdPriority());
                    return thirdCompare;
                })
                .toList();
        
        log.info("매칭 추천 기준 설정 정렬 완료: 정렬된 매니저 수={}", sorted.size());
        
        // 정렬 후 결과를 로그로 출력
        log.info("=== 정렬 후 매니저 순서 ===");
        for (int i = 0; i < sorted.size(); i++) {
            MatchingManagerListResponseDto dto = sorted.get(i);
            Long managerId = dto.getManagerId();
            log.info("{}순위: 매니저 {} (거리={}, 평점={}, 근무량={}, 리뷰수={})", 
                    i + 1,
                    managerId,
                    dto.getDistance(),
                    dto.getManagerRating(),
                    workloadMap.getOrDefault(managerId, 0.0),
                    reviewCountMap.getOrDefault(managerId, 0));
        }
        
        return sorted;
    }

    /**
     * 여러 매니저의 근무량을 한 번에 조회 (최적화)
     */
    private Map<Long, Double> getWorkloadForAllManagers(List<Long> managerIds, String workloadPeriod) {
        if (managerIds.isEmpty()) return new HashMap<>();
        
        try {
            LocalDate startDate = calculateStartDate(workloadPeriod);
            List<Object[]> results = reservationRepository.countCompletedReservationsByManagersAndPeriod(managerIds, startDate);
            
            Map<Long, Double> workloadMap = new HashMap<>();
            // 기본값 0으로 초기화
            managerIds.forEach(id -> workloadMap.put(id, 0.0));
            
            // 결과 매핑
            for (Object[] result : results) {
                Long managerId = (Long) result[0];
                Long count = (Long) result[1];
                workloadMap.put(managerId, count.doubleValue());
            }
            
            return workloadMap;
        } catch (Exception e) {
            log.warn("근무량 일괄 조회 실패: {}", e.getMessage());
            return managerIds.stream().collect(Collectors.toMap(id -> id, id -> 0.0));
        }
    }

    /**
     * 여러 매니저의 리뷰 수를 한 번에 조회 (최적화)
     */
    private Map<Long, Integer> getReviewCountForAllManagers(List<Long> managerIds) {
        if (managerIds.isEmpty()) return new HashMap<>();
        
        try {
            List<Object[]> results = reviewRepository.countReviewsByManagers(managerIds);
            
            Map<Long, Integer> reviewCountMap = new HashMap<>();
            // 기본값 0으로 초기화
            managerIds.forEach(id -> reviewCountMap.put(id, 0));
            
            // 결과 매핑
            for (Object[] result : results) {
                Long managerId = (Long) result[0];
                Long count = (Long) result[1];
                reviewCountMap.put(managerId, count.intValue());
            }
            
            return reviewCountMap;
        } catch (Exception e) {
            log.warn("리뷰 수 일괄 조회 실패: {}", e.getMessage());
            return managerIds.stream().collect(Collectors.toMap(id -> id, id -> 0));
        }
    }

    /**
     * 여러 매니저의 리뷰 요약 정보를 한 번에 조회 (최적화)
     */
    private Map<Long, ReviewSummary> getReviewSummaryForAllManagers(List<Long> managerIds) {
        if (managerIds.isEmpty()) return new HashMap<>();
        
        try {
            List<ReviewSummary> reviewSummaries = reviewSummaryRepository.findByUserIdInAndRole(managerIds, UserRole.MANAGER);
            
            Map<Long, ReviewSummary> reviewSummaryMap = new HashMap<>();
            // 기본값 null로 초기화 (리뷰가 없는 경우)
            managerIds.forEach(id -> reviewSummaryMap.put(id, null));
            
            // 결과 매핑
            for (ReviewSummary summary : reviewSummaries) {
                reviewSummaryMap.put(summary.getUserId(), summary);
            }
            
            return reviewSummaryMap;
        } catch (Exception e) {
            log.warn("리뷰 요약 일괄 조회 실패: {}", e.getMessage());
            return managerIds.stream().collect(Collectors.toMap(id -> id, id -> null));
        }
    }

    /**
     * 정렬 기준에 따른 비교 (최적화 버전 - 메모리에서만 비교)
     */
    private int compareByPriorityOptimized(
            MatchingManagerListResponseDto dto1, 
            MatchingManagerListResponseDto dto2, 
            String priority,
            Map<Long, Double> workloadMap,
            Map<Long, Integer> reviewCountMap) {
        
        int result = switch (priority.toLowerCase()) {
            case "distance" -> {
                double distance1 = Optional.ofNullable(dto1.getDistance()).orElse(Double.MAX_VALUE);
                double distance2 = Optional.ofNullable(dto2.getDistance()).orElse(Double.MAX_VALUE);
                yield Double.compare(distance1, distance2); // 거리는 작을수록 좋음
            }
            case "review" -> {
                double rating1 = dto1.getManagerRating();
                double rating2 = dto2.getManagerRating();
                yield Double.compare(rating2, rating1); // 리뷰는 클수록 좋음
            }
            case "recent" -> {
                long id1 = dto1.getManagerId();
                long id2 = dto2.getManagerId();
                yield Long.compare(id2, id1); // 최근 가입은 클수록 좋음
            }
            case "workload" -> {
                double workload1 = workloadMap.getOrDefault(dto1.getManagerId(), 0.0);
                double workload2 = workloadMap.getOrDefault(dto2.getManagerId(), 0.0);
                yield Double.compare(workload2, workload1); // 근무량은 클수록 좋음
            }
            case "review_count" -> {
                int count1 = reviewCountMap.getOrDefault(dto1.getManagerId(), 0);
                int count2 = reviewCountMap.getOrDefault(dto2.getManagerId(), 0);
                yield Integer.compare(count2, count1); // 리뷰 수는 클수록 좋음
            }
            default -> 0;
        };
        
        // 디버깅 로그 추가
        log.debug("정렬 비교 - 기준: {}, manager1: {} (값: {}), manager2: {} (값: {}), 결과: {}", 
                priority,
                dto1.getManagerId(), getValueByPriority(dto1, priority, workloadMap, reviewCountMap),
                dto2.getManagerId(), getValueByPriority(dto2, priority, workloadMap, reviewCountMap),
                result);
        
        return result;
    }
    
    private String getValueByPriority(MatchingManagerListResponseDto dto, String priority, 
                                    Map<Long, Double> workloadMap, Map<Long, Integer> reviewCountMap) {
        return switch (priority.toLowerCase()) {
            case "distance" -> String.valueOf(Optional.ofNullable(dto.getDistance()).orElse(Double.MAX_VALUE));
            case "review" -> String.valueOf(dto.getManagerRating());
            case "recent" -> String.valueOf(dto.getManagerId());
            case "workload" -> String.valueOf(workloadMap.getOrDefault(dto.getManagerId(), 0.0));
            case "review_count" -> String.valueOf(reviewCountMap.getOrDefault(dto.getManagerId(), 0));
            default -> "N/A";
        };
    }

    /**
     * 정렬 기준에 따른 비교 (기존 방식 - 비효율적, 참고용으로 주석 처리)
     */
    /*
    private int compareByPriority(MatchingManagerListResponseDto dto1, MatchingManagerListResponseDto dto2, String priority) {
        return switch (priority.toLowerCase()) {
            case "distance" -> {
                double distance1 = Optional.ofNullable(dto1.getDistance()).orElse(Double.MAX_VALUE);
                double distance2 = Optional.ofNullable(dto2.getDistance()).orElse(Double.MAX_VALUE);
                yield Double.compare(distance1, distance2); // 거리는 작을수록 좋음
            }
            case "review" -> Double.compare(dto2.getManagerRating(), dto1.getManagerRating()); // 리뷰는 클수록 좋음
            case "recent" -> Long.compare(dto2.getManagerId(), dto1.getManagerId()); // 최근 가입은 클수록 좋음
            case "workload" -> {
                double workload1 = getWorkloadValue(dto1.getManagerId());
                double workload2 = getWorkloadValue(dto2.getManagerId());
                yield Double.compare(workload2, workload1); // 근무량은 클수록 좋음
            }
            case "review_count" -> {
                int count1 = getReviewCountValue(dto1.getManagerId());
                int count2 = getReviewCountValue(dto2.getManagerId());
                yield Integer.compare(count2, count1); // 리뷰 수는 클수록 좋음
            }
            default -> 0;
        };
    }
    */

    /**
     * 매니저의 근무량 계산 (설정된 기간 기준) - 기존 방식, 비효율적
     */
    /*
    private Double getWorkloadValue(Long managerId) {
        try {
            var settings = matchingRecommendationSettingsService.getCurrentSettingsEntity();
            if (settings == null) return 0.0;
            
            LocalDate startDate = calculateStartDate(settings.getWorkloadPeriod());
            Long count = reservationRepository.countCompletedReservationsByManagerAndPeriod(managerId, startDate);
            return count.doubleValue();
        } catch (Exception e) {
            log.warn("근무량 계산 실패 for managerId={}: {}", managerId, e.getMessage());
            return 0.0;
        }
    }
    */

    /**
     * 근무량 기간에 따른 시작 날짜 계산
     */
    private LocalDate calculateStartDate(String workloadPeriod) {
        LocalDate now = LocalDate.now();
        return switch (workloadPeriod.toLowerCase()) {
            case "1week" -> now.minusWeeks(1);
            case "2week" -> now.minusWeeks(2);
            case "1month" -> now.minusMonths(1);
            case "3month" -> now.minusMonths(3);
            case "6month" -> now.minusMonths(6);
            default -> now.minusWeeks(1); // 기본값: 1주일
        };
    }

    /**
     * 매니저의 리뷰 수 계산 - 기존 방식, 비효율적
     */
    /*
    private Integer getReviewCountValue(Long managerId) {
        try {
            Long count = reviewRepository.countReviewsByManager(managerId);
            return count != null ? count.intValue() : 0;
        } catch (Exception e) {
            log.warn("리뷰 수 계산 실패 for managerId={}: {}", managerId, e.getMessage());
            return 0;
        }
    }
    */

    private List<User> getAvailableManagers(LocalDate date, LocalTime time, int duration) {
        int startTime = time.getHour() * 60 + time.getMinute();
        int endTime = startTime + duration * 60;
        List<Long> busyManagerIds = reservationRepository.findBusyManagerIds(date, startTime, endTime);

        List<User> baseManagers = busyManagerIds.isEmpty()
                ? userRepository.findByUserRole(UserRole.MANAGER)
                : userRepository.findByUserRoleAndUserIdNotIn(UserRole.MANAGER, busyManagerIds);

        List<Long> approvedManagerIds = managerDetailRepository.findByManagerStatus(ManagerStatus.APPROVED).stream()
                .map(ManagerDetail::getUserId).toList();
        return baseManagers.stream()
                .filter(user -> approvedManagerIds.contains(user.getUserId()))
                .toList();
    }

    private List<User> excludeAlreadyMatchedManagers(List<User> candidates, Long reservationId) {
        if (reservationId == null) return candidates;

        List<Long> alreadyMatched = matchingRepository.findAllByReservation_ReservationId(reservationId).stream()
                .map(m -> m.getManager().getUserId())
                .toList();

        return candidates.stream()
                .filter(user -> !alreadyMatched.contains(user.getUserId()))
                .toList();
    }

    private List<MatchingManagerListResponseDto> filterManagersByDistance(List<User> managers, Long addressId) {
        CustomerAddress address = customerAddressRepository.findById(addressId)
                .orElseThrow(() -> new NotFoundException("고객 주소가 존재하지 않습니다."));
        if (address.getCustomerLatitude() == null || address.getCustomerLongitude() == null) {
            throw new NotFoundException("고객 주소에 위경도가 존재하지 않습니다.");}

        double lat = address.getCustomerLatitude();
        double lng = address.getCustomerLongitude();
        double rangeKm = 20.0;

        Map<Long, User> userMap = managers.stream().collect(Collectors.toMap(User::getUserId, Function.identity()));
        List<ManagerDetail> managerDetails = managerDetailRepository.findByUserIdIn(userMap.keySet().stream().toList());
        
        // 리뷰 데이터 일괄 조회
        List<Long> managerIds = userMap.keySet().stream().toList();
        Map<Long, ReviewSummary> reviewSummaryMap = getReviewSummaryForAllManagers(managerIds);

        return managerDetails.stream()
                .filter(d -> d.getManagerLatitude() != null && d.getManagerLongitude() != null)
                .map(d -> {
                    double distance = calculateDistance(lat, lng, d.getManagerLatitude(), d.getManagerLongitude());
                    if (distance > rangeKm) return null;

                    User user = userMap.get(d.getUserId());
                    if (user == null) return null;
                    
                    ReviewSummary reviewSummary = reviewSummaryMap.get(d.getUserId());
                    return MatchingManagerListResponseDto.toDto(user, distance, reviewSummary);
                })
                .filter(Objects::nonNull)
                .toList();
    }

    private List<MatchingManagerListResponseDto> getFilteredManagers(LocalDate date, LocalTime time, int duration, Long addressId, boolean useDistanceFilter, Long reservationId, boolean excludeAlreadyMatched) {
        List<User> availableManagers = getAvailableManagers(date, time, duration);

        if (excludeAlreadyMatched && reservationId != null) {
            availableManagers = excludeAlreadyMatchedManagers(availableManagers, reservationId);}
        if (useDistanceFilter) {
            return filterManagersByDistance(availableManagers, addressId);}

        // 리뷰 데이터 일괄 조회
        List<Long> managerIds = availableManagers.stream().map(User::getUserId).toList();
        Map<Long, ReviewSummary> reviewSummaryMap = getReviewSummaryForAllManagers(managerIds);

        return availableManagers.stream()
                .map(user -> {
                    ReviewSummary reviewSummary = reviewSummaryMap.get(user.getUserId());
                    return MatchingManagerListResponseDto.toDto(user, null, reviewSummary);
                })
                .toList();
    }

    @Transactional
    public void adminMatchingRequest(Long matchingId) {
        Matching matching = matchingRepository.findById(matchingId).get();
        matching.setMatchingIsRequest(true);
        matching.setMatchingUpdatedAt(LocalDateTime.now());
    }

    /**
     * 관리자가 매니저 대신 수락 (매니저가 응답하지 않은 경우)
     */
    @Transactional
    public void adminAcceptMatching(Long matchingId) {
        Matching matching = matchingRepository.findById(matchingId)
                .orElseThrow(() -> new IllegalArgumentException("매칭 정보를 찾을 수 없습니다."));
        
        // 매니저가 이미 응답했으면 처리 불가
        if (matching.getMatchingManagerIsAccept() != null) {
            throw new IllegalStateException("매니저가 이미 응답한 매칭입니다.");
        }
        
        // 매칭 요청이 보내지지 않았으면 처리 불가
        if (!Boolean.TRUE.equals(matching.getMatchingIsRequest())) {
            throw new IllegalStateException("매칭 요청이 보내지지 않은 상태입니다.");
        }
        
        // 관리자가 매니저 대신 수락 처리
        matching.setMatchingManagerIsAccept(true);
        matching.setMatchingUpdatedAt(LocalDateTime.now());
        
        // 수요자에게 매니저가 수락했다는 알림 전송
        alertService.sendAlert(
            matching.getReservation().getCustomer().getUserId(), 
            AlertTrigger.MATCHING_ACCEPTED_BY_MANAGER,
            matching.getReservation().getReservationId()
        );
        
        log.info("👨‍💼 관리자가 매니저 대신 수락: matchingId={}, reservationId={}, managerId={}", 
                matchingId, matching.getReservation().getReservationId(), matching.getManager().getUserId());
    }

    @Transactional
    public void adminAddMatching(Long reservationId, Long managerId) {
        Matching matching = Matching.builder()
                .reservation(reservationRepository.findById(reservationId).get())
                .manager(userRepository.findById(managerId).get())
                .matchingPriority(matchingRepository.findMaxMatchingPriorityByReservationId(reservationId) + 1)
                .matchingIsRequest(false)
                .matchingUpdatedAt(LocalDateTime.now())
                .build();

        matchingRepository.save(matching);
    }

    @Transactional
    public void adminAddMatchingAuto(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약이 존재하지 않습니다."));

        MatchingRequestDto matchingRequestDto = MatchingRequestDto.builder()
                .reservationId(reservation.getReservationId())
                .addressId(reservation.getAddress().getAddressId())
                .reservationDate(reservation.getReservationDate())
                .reservationTime(reservation.getReservationTime())
                .reservationDuration(reservation.getReservationDuration())
                .build();

        List<Long> managerIds = selectTop3Candidate(matchingRequestDto, "custom", true, false).stream()
                .map(MatchingManagerListResponseDto::getManagerId).toList();

        for (Long managerId : managerIds) {
            adminAddMatching(reservationId, managerId);
        }
    }
}