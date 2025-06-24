package com.antmen.antwork.manager.controller;

import com.antmen.antwork.common.api.response.calculation.CalculationListWithTotalDto;
import com.antmen.antwork.common.api.response.calculation.CalculationResponseDto;
import com.antmen.antwork.common.service.CalculationService;
import com.antmen.antwork.common.util.AuthUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/manager/calculation")
@RequiredArgsConstructor
public class ManagerCalculationController {
    private final CalculationService calculationService;

    // 나의 정산 목록 조회
    @GetMapping("/my")
    public ResponseEntity<List<CalculationResponseDto>> getMyCalculations(
            @AuthenticationPrincipal AuthUserDto authUserDto) {
        Long loginId = authUserDto.getUserIdAsLong();
        return ResponseEntity.ok(calculationService.getCalculationsById(loginId));
    }

    @GetMapping("/my/summary")
    public ResponseEntity<CalculationListWithTotalDto> getManagerCalculationsWithTotal(
            @AuthenticationPrincipal AuthUserDto authUserDto,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate EndDate) {
        Long loginId = authUserDto.getUserIdAsLong();
        return ResponseEntity.ok(calculationService.getManagerCalculationsWithTotal(loginId, startDate, EndDate));
    }

    // 이전 정산 내역 조회
    @GetMapping("/history")
    public ResponseEntity<List<CalculationResponseDto>> getCalculationHistory(
            @AuthenticationPrincipal AuthUserDto authUserDto) {
        Long managerId = authUserDto.getUserIdAsLong();
        List<CalculationResponseDto> result = calculationService.getCalculationHistory(managerId);
        return ResponseEntity.ok(result);
    }

    // 정산 요청 API (주급 기준, 이번 주 제외)
    @PostMapping("/request")
    public ResponseEntity<CalculationListWithTotalDto> requestCalculation(
            @AuthenticationPrincipal AuthUserDto authUserDto,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        Long userId = authUserDto.getUserIdAsLong();
        CalculationListWithTotalDto result = calculationService.requestCalculation(userId, startDate, endDate);
        return ResponseEntity.ok(result);
    }
}
