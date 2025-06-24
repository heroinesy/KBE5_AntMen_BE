package com.antmen.antwork.admin.controller;

import com.antmen.antwork.common.api.response.calculation.CalculationListWithTotalDto;
import com.antmen.antwork.common.api.response.calculation.CalculationResponseDto;
import com.antmen.antwork.common.service.CalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/calculation")
@RequiredArgsConstructor
public class AdminCaculationController {
    private final CalculationService calculationService;

    // 전체 정산 목록 조회 (관리자)
    @GetMapping
    public ResponseEntity<Page<CalculationResponseDto>> getCalculations(Pageable pageable) {
        return ResponseEntity.ok(calculationService.getCalculations(pageable));
    }

    // 매니저 별 정산 목록 (관리자)
    @GetMapping("/{managerId}")
    public ResponseEntity<List<CalculationResponseDto>> getCalculationsByManager(
            @PathVariable Long managerId
    ) {
        return ResponseEntity.ok(calculationService.getCalculationsById(managerId));
    }

    // 매니저의 기간별 총 합계, 정산목록 (관리자)
    @GetMapping("/{managerId}/summary")
    public ResponseEntity<CalculationListWithTotalDto> getManagerCalculationsWithTotal(
            @PathVariable Long managerId,
            @RequestParam LocalDate weekStart,
            @RequestParam LocalDate weekEnd) {
        return ResponseEntity.ok(calculationService.getManagerCalculationsWithTotal(managerId, weekStart, weekEnd));
    }
}
