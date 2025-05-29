package com.antmen.antwork.customer.api.controller;

import com.antmen.antwork.customer.api.request.MatchingRequestDto;
import com.antmen.antwork.customer.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/matching")
@RequiredArgsConstructor
public class MatchingController {
    private final MatchingService matchingService;
    // 매칭 생성
    @PostMapping
    public void createMatching(@RequestBody MatchingRequestDto matchingRequestDto) {
        matchingService.createMatching(matchingRequestDto);
    }


}
