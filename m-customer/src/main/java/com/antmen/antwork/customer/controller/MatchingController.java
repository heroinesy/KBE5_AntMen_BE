package com.antmen.antwork.customer.controller;

import com.antmen.antwork.common.api.request.MatchingCancelRequestDto;
import com.antmen.antwork.common.api.request.MatchingRequestDto;
import com.antmen.antwork.common.api.request.MatchingResponseRequestDto;
import com.antmen.antwork.common.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/matching")
@RequiredArgsConstructor
public class MatchingController {
    private final MatchingService matchingService;

    // 매칭 생성
    @PostMapping
    public ResponseEntity createMatching(@RequestBody MatchingRequestDto matchingRequestDto) {
        matchingService.initiateMatching(matchingRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{matchingId}")
    public ResponseEntity respondToMatching(@PathVariable Long matchingId, @RequestBody MatchingResponseRequestDto matchingRequestDto) {
        matchingService.customerResponseMatching(matchingId, matchingRequestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/{matchingId}/cancel")
    public ResponseEntity canceledMatching(@PathVariable Long matchingId, @RequestBody MatchingCancelRequestDto matchingCancelDto) {

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
