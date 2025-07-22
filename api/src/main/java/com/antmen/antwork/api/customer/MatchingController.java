package com.antmen.antwork.api.customer;












@RestController
@RequestMapping("/api/v1/matchings")
@RequiredArgsConstructor
public class MatchingController {
    private final MatchingService matchingService;

    @PutMapping("/{matchingId}")
    public ResponseEntity respondToMatching(@PathVariable Long matchingId, @RequestBody MatchingResponseRequestDto matchingRequestDto) {
        matchingService.customerResponseMatching(matchingId, matchingRequestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 조회지만 매개변수를 사용해야해서 Post방식 이용
    @PostMapping
    public ResponseEntity<List<MatchingManagerListResponseDto>> showManagerList(
            @RequestBody MatchingRequestDto matchingRequestDto,
            @RequestParam(defaultValue = "true") boolean useDistanceFilter,
            @RequestParam(defaultValue = "distance") String sortType
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(matchingService.getManagerList(matchingRequestDto, useDistanceFilter, sortType));
    }
}