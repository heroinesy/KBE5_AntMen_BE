package com.antmen.antwork.api.admin;








@RestController
@RequestMapping("/api/v1/admin/calculations")
@RequiredArgsConstructor
public class AdminCalculationController {
    private final AdminCalculationService adminCalculationService;

    // 전체 정산 목록 조회 (관리자)
    @GetMapping
    public ResponseEntity<AdminSummaryCalculationResponseDto> getSummary(){
        return ResponseEntity.ok(adminCalculationService.getCalculationSummary());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminCalculationResponseDto> getCalculationDetail(@PathVariable Long id){
        return ResponseEntity.ok(adminCalculationService.getCalculationDetail(id));
    }

//    // 매니저 별 정산 목록 (관리자)
//    @GetMapping("/{managerId}")
//    public ResponseEntity<List<CalculationResponseDto>> getCalculationsByManager(
//            @PathVariable Long managerId
//    ) {
//        return ResponseEntity.ok(calculationService.getCalculationsById(managerId));
//    }
//
//    // 매니저의 기간별 총 합계, 정산목록 (관리자)
//    @GetMapping("/{managerId}/summary")
//    public ResponseEntity<CalculationListWithTotalDto> getManagerCalculationsWithTotal(
//            @PathVariable Long managerId,
//            @RequestParam LocalDate weekStart,
//            @RequestParam LocalDate weekEnd) {
//        return ResponseEntity.ok(calculationService.getManagerCalculationsWithTotal(managerId, weekStart, weekEnd));
//    }
}