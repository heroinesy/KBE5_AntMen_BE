package com.antmen.antwork.customer.service;

import com.antmen.antwork.common.api.request.AlertRequestDto;
import com.antmen.antwork.common.domain.entity.Matching;
import com.antmen.antwork.common.domain.entity.Reservation;
import com.antmen.antwork.common.infra.repository.ReservationRepository;
import com.antmen.antwork.common.infra.repository.UserRepository;
import com.antmen.antwork.common.service.AlertService;
import com.antmen.antwork.customer.api.request.MatchingRequestDto;
import com.antmen.antwork.common.infra.repository.MatchingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchingService {
    private final MatchingRepository matchingRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final AlertService alertService;

    // 매칭 생성
    @Transactional
    public void initiateMatching(MatchingRequestDto matchingRequestDto) {
        Reservation reservation = reservationRepository.findById(matchingRequestDto.getReservationId())
                .orElseThrow(() -> new IllegalArgumentException("예약이 존재하지 않습니다."));

        List<Matching> matchingList = new ArrayList<>();
        int basePriority = 1;
        List<Long> managerIds = matchingRequestDto.getManagerIds();

        // 자동추천
        if (managerIds == null || managerIds.isEmpty()) {
            managerIds = selectTop3Candidate();
        }

        for (int i = 0; i < managerIds.size(); i++) {
            Matching matching = Matching.builder()
                    .reservation(reservation)
                    .manager(userRepository.findById(managerIds.get(i))
                            .orElseThrow(() -> new IllegalArgumentException("매니저가 없습니다.")))
                    .matchingPriority(basePriority + i)
                    .build();
            matchingList.add(matching);
        }

        matchingRepository.saveAll(matchingList);

        // 1순위에게 알림 전송
        if (!matchingList.isEmpty()) {
            Matching top = matchingList.get(0); // 우선순위대로 추가했으므로 첫 번째가 최우선
            top.setMatchingRequestAt(LocalDateTime.now());

            alertService.sendAlert(AlertRequestDto.builder()
                    .userId(top.getManager().getUserId())
                    // 예약 상세내용도 보내줘야하나?
                    .alertContent("매칭 요청이 왔어요.")
                    .alertTrigger("Matching")
                    .build());
        }
    }

    // 자동추천 3명
    @Transactional
    public List<Long> selectTop3Candidate( ) {
        // 추후에 조건추가 예정
        return userRepository.findTop3AvailableManagers((Pageable) PageRequest.of(0, 3));
    }
}
