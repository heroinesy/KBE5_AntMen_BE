package com.antmen.antwork.manager.controller;

import com.antmen.antwork.common.api.request.MatchingManagerRequestDto;
import com.antmen.antwork.common.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager/v1/matching")
@RequiredArgsConstructor
public class MatchingManagerController {

    public final MatchingService matchingService;

    // 매니저의 매칭 확인
    @PutMapping("/{matchingId}")
    public ResponseEntity respondToMatchingManager(@PathVariable Long matchingId, @RequestBody MatchingManagerRequestDto matchingManagerRequestDto) {
        matchingService.managerRespondMatching(matchingId, matchingManagerRequestDto);
        return ResponseEntity.ok().build();
    }
}
