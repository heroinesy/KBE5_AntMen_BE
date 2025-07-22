package com.antmen.antwork.api.common;











@RestController
@RequestMapping("/api/v1/common/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@AuthenticationPrincipal AuthUserDto authUserDto) {
        return alertService.subscribe(authUserDto.getUserIdAsLong());
    }

    @GetMapping("")
    public ResponseEntity showAllAlerts(@AuthenticationPrincipal AuthUserDto authUserDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(alertService.getAlertList(authUserDto.getUserIdAsLong()));
    }

    @GetMapping("/{alertId}")
    public ResponseEntity<?> showAlertById(@AuthenticationPrincipal AuthUserDto authUserDto,@PathVariable Long alertId) {
        return ResponseEntity.ok(alertService.getAlert(authUserDto.getUserIdAsLong(), alertId));
    }

    @PatchMapping("")
    public ResponseEntity readAllAlert(@AuthenticationPrincipal AuthUserDto authUserDto) {
        alertService.readAllAlert(authUserDto.getUserIdAsLong());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(alertService.getAlertList(authUserDto.getUserIdAsLong()));
    }

    @PatchMapping("/{alertId}/read")
    public ResponseEntity readAlert(@AuthenticationPrincipal AuthUserDto authUserDto, @PathVariable Long alertId) {
        alertService.readAlert(authUserDto.getUserIdAsLong(),alertId);
        return ResponseEntity.ok().build();
    }
}