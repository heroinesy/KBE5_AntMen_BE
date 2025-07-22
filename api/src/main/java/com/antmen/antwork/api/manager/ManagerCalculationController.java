package com.antmen.antwork.api.manager;













@RestController
@RequestMapping("/api/v1/manager/calculation")
@RequiredArgsConstructor
public class ManagerCalculationController {
    private final ManagerCalculationService managerCalculationService;

    // 정산 상세 내역
   @GetMapping("/my/summary")
    public ResponseEntity<ManagerCalculationListWithTotalDto> getManagerCalculationsWithTotal(
            @AuthenticationPrincipal AuthUserDto authUserDto,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate EndDate) {
        Long loginId = authUserDto.getUserIdAsLong();
        return ResponseEntity.ok(managerCalculationService.getManagerCalculationsWithTotal(loginId, startDate, EndDate));
    }

    // 이전 정산 내역 조회
    @GetMapping("/history")
    public ResponseEntity<List<ManagerCalculationResponseDto>> getCalculationHistory(
            @AuthenticationPrincipal AuthUserDto authUserDto) {
        Long managerId = authUserDto.getUserIdAsLong();
        List<ManagerCalculationResponseDto> result = managerCalculationService.getCalculationHistory(managerId);
        return ResponseEntity.ok(result);
    }

    // 정산 요청 API (주급 기준, 이번 주 제외)
    @PostMapping("/request")
    public ResponseEntity<ManagerCalculationListWithTotalDto> requestCalculation(
            @AuthenticationPrincipal AuthUserDto authUserDto,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        Long userId = authUserDto.getUserIdAsLong();
        ManagerCalculationListWithTotalDto result = managerCalculationService.requestCalculation(userId, startDate, endDate);
        return ResponseEntity.ok(result);
    }
}