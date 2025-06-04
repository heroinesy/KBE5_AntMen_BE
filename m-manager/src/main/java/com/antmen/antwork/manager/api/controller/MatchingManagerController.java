package com.antmen.antwork.manager.api.controller;

import com.antmen.antwork.manager.api.request.MatchingManagerRequestDto;
import com.antmen.antwork.manager.service.MatchingManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager/v1/matching")
@RequiredArgsConstructor
public class MatchingManagerController {

    /*
    public final MatchingManagerService matchingService;

    // 매니저의 매칭 확인
    @PutMapping("/{matchingId}")
    public ResponseEntity respondToMatchingManager(@PathVariable Long matchingId, @RequestBody MatchingManagerRequestDto matchingManagerRequestDto) {
        matchingService.respondToMatching(matchingId, matchingManagerRequestDto);
        return ResponseEntity.ok().build();
    }

     */

    @GetMapping("/test")
    public String test() {
        return "test";
    }
    }
