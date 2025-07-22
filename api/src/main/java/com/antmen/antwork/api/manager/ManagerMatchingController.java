package com.antmen.antwork.api.manager;
















@RestController
@RequestMapping("/v1/manager/matching")
@RequiredArgsConstructor
public class ManagerMatchingController {

    public final MatchingService matchingService;
    public final ReservationService reservationService;

    // 매니저의 매칭 확인
    @PutMapping("/{matchingId}")
    public ResponseEntity respondToMatchingManager(@PathVariable Long matchingId,
            @RequestBody MatchingManagerRequestDto matchingManagerRequestDto) {
        matchingService.managerRespondMatching(matchingId, matchingManagerRequestDto);
        return ResponseEntity.ok().build();
    }

    // 매칭 요청 목록 조회
    // 페이징이 필요한지 모르겠어용 추후에 제거하든 수정
    @GetMapping("/list")
    public ResponseEntity<Page<ReservationHistoryDto>> getMatchingList(
            @AuthenticationPrincipal AuthUserDto authUserDto,
            @RequestParam(defaultValue = "0") int page) {

        if (authUserDto == null || authUserDto.getUserIdAsLong() == null) {
            throw new IllegalArgumentException("인증 정보가 없습니다.");
        }

        Long managerId = authUserDto.getUserIdAsLong();

        Pageable pageable = PageRequest.of(page, 5);
        return ResponseEntity.ok(reservationService.getReservationsByMatchingManager(managerId, pageable));
    }
}
