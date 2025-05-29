package com.antmen.antwork.customer.api.controller;

import com.antmen.antwork.customer.api.request.MatchingRequestDto;
import com.antmen.antwork.customer.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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


}
