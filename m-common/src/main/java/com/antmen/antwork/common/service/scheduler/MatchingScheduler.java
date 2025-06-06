package com.antmen.antwork.common.service.scheduler;

import com.antmen.antwork.common.api.request.alert.AlertRequestDto;
import com.antmen.antwork.common.domain.entity.reservation.Matching;
import com.antmen.antwork.common.domain.entity.reservation.Reservation;
import com.antmen.antwork.common.domain.entity.reservation.ReservationStatus;
import com.antmen.antwork.common.infra.repository.reservation.MatchingRepository;
import com.antmen.antwork.common.service.AlertService;
import com.antmen.antwork.common.service.serviceReservation.MatchingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class MatchingScheduler {
    private final MatchingRepository matchingRepository;
    private final MatchingService matchingService;
    private final AlertService alertService;

    // 제한 시간동안 매니저가 답장이 없을 때
    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void expireUnanweredMatchingRequests() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(15);

        List<Matching> expiredMatchings = matchingRepository
                .findLatestPendingMatchings(threshold);

        for (Matching matching : expiredMatchings) {
            if (matching.getReservation().getReservationStatus() == ReservationStatus.WAITING) {
                matchingService.triggerNextMatching(matching);
            }
        }
    }

    // 제한 시간 동안 수요자가 답장이 없을 때
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void pendingUnansweredCustomerMatching() {
        List<Matching> pendingCustomerMatchings = matchingRepository
                .findAllByMatchingManagerIsAcceptIsTrueAndMatchingIsFinalIsNull();

        for (Matching matching : pendingCustomerMatchings) {
            Reservation reservation = matching.getReservation();

            LocalDateTime baseTime = matching.getMatchingUpdatedAt();
            LocalDateTime now = LocalDateTime.now();
            Duration untilWork = Duration.between(
                    now,
                    LocalDateTime.of(reservation.getReservationDate(), reservation.getReservationTime())
            );

            Duration allowed = untilWork.toHours() >= 24
                    ? Duration.ofHours(24)
                    : Duration.ofHours(1);

            Duration diff = Duration.between(baseTime, now);

            if (diff.compareTo(allowed) > 0) {
                log.info("⏰ 수요자 응답 시간 초과 → 자동 거절: matchingId={}, reservationId={}, ",matching.getMatchingId(), reservation.getReservationId());
                matching.setMatchingIsFinal(false);
                reservation.setReservationCancelReason("자동 거절 (수요자 응답 시간 초과)");

                reservation.setReservationStatus(ReservationStatus.CANCEL);

                alertService.sendAlert(AlertRequestDto.builder()
                                .alertContent("매칭 요청에 응답하지 않아 자동으로 예약이 취소되었습니다.")
                                .alertTrigger("Matching")
                        .build());

                matching.setMatchingUpdatedAt(LocalDateTime.now());
            }

            // 취소된 예약에 대해 매니저들에게 예약 취소 알람
            List<Matching> requestedMatching = matchingRepository.findAllByReservation_ReservationId(reservation.getReservationId());

            for (Matching m : requestedMatching) {
                m.setMatchingIsFinal(false);
                m.setMatchingRefuseReason("자동 거절 (수요자 응답 시간 초과)");
                m.setMatchingUpdatedAt(LocalDateTime.now());
                if (m.getMatchingIsRequest()) {
                    if (m.getMatchingManagerIsAccept() || m.getMatchingManagerIsAccept() == null) {
                        alertService.sendAlert(AlertRequestDto.builder()
                                .userId(m.getManager().getUserId())
                                .alertContent("고객이 취소한 예약입니다.")
                                .alertTrigger("Matching")
                                .build());
                    }
                }
            }
        }

    }
}
