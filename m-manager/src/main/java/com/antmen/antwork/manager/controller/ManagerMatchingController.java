package com.antmen.antwork.manager.controller;

import com.antmen.antwork.common.api.request.reservation.MatchingManagerRequestDto;
import com.antmen.antwork.common.api.response.reservation.ReservationHistoryDto;
import com.antmen.antwork.common.service.serviceReservation.MatchingService;
import com.antmen.antwork.common.service.serviceReservation.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/manager/matching")
@RequiredArgsConstructor
public class ManagerMatchingController {

    public final MatchingService matchingService;
    public final ReservationService reservationService;

    // 매니저의 매칭 확인
    @PutMapping("/{matchingId}")
    public ResponseEntity respondToMatchingManager(@PathVariable Long matchingId, @RequestBody MatchingManagerRequestDto matchingManagerRequestDto) {
        matchingService.managerRespondMatching(matchingId, matchingManagerRequestDto);
        return ResponseEntity.ok().build();
    }

/* 우선 주석처리해서 빌드해놓을게용 :)
    // 매칭 리스트 불러오기
    @GetMapping("/list")
    public ResponseEntity getMatchingList(@AuthenticationPrincipal Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(matchingService.getMatchingRequestList(id));
    }*/

    // 매칭 예약 리스트 조회 대체 ( 추후 확인하시고 대체 부탁드립니다 :) )
    @GetMapping("/list")
    public ResponseEntity<List<ReservationHistoryDto>> getMatchingList(@AuthenticationPrincipal Long id) {
        return ResponseEntity.ok(reservationService.getReservationsByMatchingManager(id));
    }

    // TODO: 예약 확인하기 -> 예약 단건 조회 이용할 수 있으면 이용하기

}
