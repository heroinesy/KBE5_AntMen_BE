package com.antmen.antwork.common.api.controller;

import com.antmen.antwork.common.api.request.alert.AlertRequestDto;
import com.antmen.antwork.common.api.response.alert.AlertEvent;
import com.antmen.antwork.common.service.AlertService;
import com.antmen.antwork.common.util.AuthUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/common/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;
    private final ApplicationEventPublisher eventPublisher;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@AuthenticationPrincipal AuthUserDto authUserDto) {
        return alertService.subscribe(authUserDto.getUserIdAsLong());
    }

//    @PostMapping
//    public void createAlert(@RequestBody AlertRequestDto alertRequestDto) {
//        alertService.sendAlert(alertRequestDto);
//    }

    // Postman 테스트도 이벤트 발행 방식으로 변경
    @Transactional
    @PostMapping
    public void createAlert(@RequestBody AlertRequestDto alertRequestDto) {
        eventPublisher.publishEvent(AlertEvent.builder()
                .userId(alertRequestDto.getUserId())
                .alertContent(alertRequestDto.getAlertContent())
                .alertTrigger(alertRequestDto.getAlertTrigger())
                .redirectUrl(alertRequestDto.getRedirectUrl())
                .build());
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
