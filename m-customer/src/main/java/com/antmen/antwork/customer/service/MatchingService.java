package com.antmen.antwork.customer.service;

import com.antmen.antwork.common.domain.entity.Matching;
import com.antmen.antwork.common.domain.entity.Reservation;
import com.antmen.antwork.common.domain.entity.User;
import com.antmen.antwork.common.infra.repository.ReservationRepository;
import com.antmen.antwork.common.infra.repository.UserRepository;
import com.antmen.antwork.customer.api.request.MatchingRequestDto;
import com.antmen.antwork.customer.infra.repository.MatchingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchingService {
    private final MatchingRepository matchingRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    // 매칭 생성
    public void createMatching(MatchingRequestDto matchingRequestDto) {
        Reservation reservation = reservationRepository.findById(matchingRequestDto.getResevationId())
                .orElseThrow(() -> new IllegalArgumentException("예약이 존재하지 않습니다."));

        List<Long> managerIds = matchingRequestDto.getManagerIds();

        List<Matching> matchingList = new ArrayList<>();
        for (int i = 0; i < managerIds.size(); i++) {
            Matching matching = Matching.builder()
                    .reservation(reservation)
                    .manager(userRepository.findById(managerIds.get(i))
                            .orElseThrow(() -> new IllegalArgumentException("매니저가 없습니다.")))
                    .matchingPriority(i + 1)
                    .build();
            matchingList.add(matching);
        }
        matchingRepository.saveAll(matchingList);
    }
}
