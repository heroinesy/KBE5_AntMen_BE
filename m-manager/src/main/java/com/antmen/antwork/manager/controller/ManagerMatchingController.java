package com.antmen.antwork.manager.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    // 매칭 요청 목록 조회
    // 페이징이 필요한지 모르겠어용 추후에 제거하든 수정
    @GetMapping("/list")
    public ResponseEntity<Page<ReservationHistoryDto>> getMatchingList(
            @AuthenticationPrincipal AuthUserDto authUserDto,
            @RequestParam(defaultValue = "0") int page) {

        if (authUserDto == null || authUserDto.getUserIdAsLong() == null) {
            throw new IllegalArgumentException("인증 정보가 없습니다.");
        }

        Long managerId = authUserDto.getUserIdAsLong();

        Pageable pageable = PageRequest.of(page, 5);
        return ResponseEntity.ok(reservationService.getReservationsByMatchingManager(managerId, pageable));
    }
}
