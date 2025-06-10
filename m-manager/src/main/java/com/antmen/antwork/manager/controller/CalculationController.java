package com.antmen.antwork.manager.controller;

import com.antmen.antwork.common.api.response.calculation.CalculationListWithTotalDto;
import com.antmen.antwork.common.api.response.calculation.CalculationResponseDto;
import com.antmen.antwork.common.domain.entity.reservation.Calculation;
import com.antmen.antwork.common.infra.repository.reservation.ReservationRepository;
import com.antmen.antwork.common.service.CalculationService;
import com.antmen.antwork.common.util.AuthUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/manager/calculation")
@RequiredArgsConstructor
public class CalculationController {

    private final CalculationService calculationService;

    // 전체 정산 목록 조회
    @GetMapping
    public ResponseEntity<List<CalculationResponseDto>> getCalculations(Pageable pageable) {
        return ResponseEntity.ok(calculationService.getCalculations(pageable));
    }

    // 나의 정산 목록 조회
    @GetMapping("/my")
    public ResponseEntity<List<CalculationResponseDto>> getMyCalculations(
            @AuthenticationPrincipal AuthUserDto authUserDto
    ) {
        Long loginId = authUserDto.getUserIdAsLong();
        return ResponseEntity.ok(calculationService.getCalculationsById(loginId));
    }

    // 매니저 별 정산 목록
    @GetMapping("/{managerId}")
    public ResponseEntity<List<CalculationResponseDto>> getCalculationsByManager(
            @PathVariable Long managerId
    ) {
        return ResponseEntity.ok(calculationService.getCalculationsById(managerId));
    }

    // 매니저의 기간별 총 합계, 정산목록
    @GetMapping("/{managerId}/summary")
    public ResponseEntity<CalculationListWithTotalDto> getManagerCalculationsWithTotal(
            @PathVariable Long managerId,
            @RequestParam LocalDate weekStart,
            @RequestParam LocalDate weekEnd) {
        return ResponseEntity.ok(calculationService.getManagerCalculationsWithTotal(managerId,weekStart,weekEnd));
    }


}
