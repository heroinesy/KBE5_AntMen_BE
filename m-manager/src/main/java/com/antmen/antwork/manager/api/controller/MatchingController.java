package com.antmen.antwork.manager.api.controller;

import com.antmen.antwork.manager.api.request.MatchingManagerRequestDto;
import com.antmen.antwork.manager.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager/v1/matching")
@RequiredArgsConstructor
public class MatchingController {

    public final MatchingService matchingService;
    // 매니저의 매칭 수락
    @PutMapping("/{matchingId}")
    public void respondToMatching(@PathVariable Long matchingId, @RequestBody MatchingManagerRequestDto matchingManagerRequestDto) {

    }
}
