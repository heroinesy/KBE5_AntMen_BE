package com.antmen.antwork.customer.service.scheduler;

import com.antmen.antwork.common.api.request.AlertRequestDto;
import com.antmen.antwork.common.domain.entity.Matching;
import com.antmen.antwork.common.domain.entity.Reservation;
import com.antmen.antwork.common.domain.entity.ReservationStatus;
import com.antmen.antwork.common.infra.repository.MatchingRepository;
import com.antmen.antwork.common.service.AlertService;
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
    private final AlertService alertService;

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
