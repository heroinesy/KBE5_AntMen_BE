package com.antmen.antwork.manager.controller;

import com.antmen.antwork.common.api.request.reservation.MatchingManagerRequestDto;
import com.antmen.antwork.common.service.serviceReservation.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/manager/matching")
@RequiredArgsConstructor
public class ManagerMatchingController {

    public final MatchingService matchingService;

    // 매니저의 매칭 확인
    @PutMapping("/{matchingId}")
    public ResponseEntity respondToMatchingManager(@PathVariable Long matchingId, @RequestBody MatchingManagerRequestDto matchingManagerRequestDto) {
        matchingService.managerRespondMatching(matchingId, matchingManagerRequestDto);
        return ResponseEntity.ok().build();
    }

    // 매칭 리스트 불러오기
    @GetMapping("/list")
    public ResponseEntity getMatchingList(@AuthenticationPrincipal Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(matchingService.getMatchingRequestList(id));
    }

    // TODO: 예약 확인하기 -> 예약 단건 조회 이용할 수 있으면 이용하기

}
