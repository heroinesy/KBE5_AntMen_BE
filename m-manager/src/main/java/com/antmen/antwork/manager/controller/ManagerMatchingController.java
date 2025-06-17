package com.antmen.antwork.manager.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.antmen.antwork.common.api.request.reservation.MatchingManagerRequestDto;
import com.antmen.antwork.common.api.response.reservation.ReservationHistoryDto;
import com.antmen.antwork.common.service.serviceReservation.MatchingService;
import com.antmen.antwork.common.service.serviceReservation.ReservationService;
import com.antmen.antwork.common.util.AuthUserDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/manager/matching")
@RequiredArgsConstructor
public class ManagerMatchingController {

    public final MatchingService matchingService;
    public final ReservationService reservationService;

    // 매니저의 매칭 확인
    @PutMapping("/{matchingId}")
    public ResponseEntity respondToMatchingManager(@PathVariable Long matchingId,
            @RequestBody MatchingManagerRequestDto matchingManagerRequestDto) {
        matchingService.managerRespondMatching(matchingId, matchingManagerRequestDto);
        return ResponseEntity.ok().build();
    }

    /*
     * 우선 주석처리해서 빌드해놓을게용 :)
     * // 매칭 리스트 불러오기
     * 
     * @GetMapping("/list")
     * public ResponseEntity getMatchingList(@AuthenticationPrincipal Long id) {
     * return ResponseEntity
     * .status(HttpStatus.OK)
     * .body(matchingService.getMatchingRequestList(id));
     * }
     */

    @GetMapping("/list")
    public ResponseEntity<List<ReservationHistoryDto>> getMatchingList(
            @AuthenticationPrincipal AuthUserDto authUserDto) {

        if (authUserDto == null || authUserDto.getUserIdAsLong() == null) {
            throw new IllegalArgumentException("인증 정보가 없습니다.");
        }
        Long managerId = authUserDto.getUserIdAsLong();
        return ResponseEntity.ok(reservationService.getReservationsByMatchingManager(managerId));
    }

    // TODO: 예약 확인하기 -> 예약 단건 조회 이용할 수 있으면 이용하기

}
