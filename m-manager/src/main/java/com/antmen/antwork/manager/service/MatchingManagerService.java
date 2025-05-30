package com.antmen.antwork.manager.service;

import com.antmen.antwork.common.api.request.AlertRequestDto;
import com.antmen.antwork.common.domain.constant.ReservationConstants;
import com.antmen.antwork.common.domain.entity.Matching;
import com.antmen.antwork.common.infra.repository.MatchingRepository;
import com.antmen.antwork.common.service.AlertService;
import com.antmen.antwork.manager.api.request.MatchingManagerRequestDto;
import com.antmen.antwork.customer.service.MatchingService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchingManagerService {

    private final MatchingRepository matchingRepository;
    private final AlertService alertService;
    private final MatchingService customerMatchingService;

    @Transactional
    public void respondToMatching(Long matchingId, MatchingManagerRequestDto matchingManagerRequestDto) {
        Matching matching = matchingRepository.findById(matchingId)
                .orElseThrow(() -> new IllegalArgumentException("매칭 정보를 찾을 수 없습니다."));

        if (matching.getMatchingManagerIsAccept() != null) {
            throw new IllegalStateException("이미 응답한 매칭입니다.");
        }

        matching.setMatchingManagerIsAccept(matchingManagerRequestDto.getMatchingManagerIsAccept());

        if (matchingManagerRequestDto.getMatchingRefuseReason() != null) {
            matching.setMatchingRefuseReason(matchingManagerRequestDto.getMatchingRefuseReason());
        }

        matching.setMatchingUpdatedAt(LocalDateTime.now());

        // 수락시 수요자에게 알림
        if (matching.getMatchingManagerIsAccept()) {
            alertService.sendAlert(AlertRequestDto.builder()
                    .userId(matching.getReservation().getCustomer().getUserId())
                    .alertContent("매칭이 수락되었습니다.")
                    .alertTrigger("Matching")
                    .build());
        } else {
            // 거절시 다음 순위로 넘어감
            customerMatchingService.triggerNextMatching(matching);
        }
    }

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void expireUnanweredMatchingRequests() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(15);

        List<Matching> expiredMatchings = matchingRepository
                .findLatestPendingMatchings(threshold);

        for (Matching matching : expiredMatchings) {
            if (matching.getReservation().getReservationStatus() == ReservationConstants.STATUS_WAITING) {
                customerMatchingService.triggerNextMatching(matching);
            }
        }
    }
}