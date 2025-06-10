package com.antmen.antwork.customer.controller;

import com.antmen.antwork.common.api.request.reservation.MatchingCancelRequestDto;
import com.antmen.antwork.common.api.request.reservation.MatchingRequestDto;
import com.antmen.antwork.common.api.request.reservation.MatchingResponseRequestDto;
import com.antmen.antwork.common.service.serviceReservation.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/matchings")
@RequiredArgsConstructor
public class MatchingController {
    private final MatchingService matchingService;

    // 매칭 생성
//    @PostMapping
//    public ResponseEntity createMatching(@RequestBody MatchingRequestDto matchingRequestDto) {
//        matchingService.initiateMatching(matchingRequestDto);
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }

    @PutMapping("/{matchingId}")
    public ResponseEntity respondToMatching(@PathVariable Long matchingId, @RequestBody MatchingResponseRequestDto matchingRequestDto) {
        matchingService.customerResponseMatching(matchingId, matchingRequestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 필요한지 모르겠음 흠...
    @PutMapping("/{matchingId}/cancel")
    public ResponseEntity canceledMatching(@PathVariable Long matchingId, @RequestBody MatchingCancelRequestDto matchingCancelDto) {

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 조회지만 매개변수를 사용해야해서 Post방식 이용
    @PostMapping
    public ResponseEntity showManagerList(@RequestBody MatchingRequestDto matchingRequestDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(matchingService.getManagerList(matchingRequestDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity showManagerDetail(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
