package com.antmen.antwork.common.api.controller;

import com.antmen.antwork.common.service.AlertService;
import com.antmen.antwork.common.util.AuthUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/common/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @GetMapping("")
    public ResponseEntity showAllAlerts(@AuthenticationPrincipal AuthUserDto authUserDto) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(alertService.getAlertList(authUserDto.getUserIdAsLong()));
    }

    @PutMapping("")
    public ResponseEntity readAllAlert(@AuthenticationPrincipal AuthUserDto authUserDto) {
        alertService.readAllAlert(authUserDto.getUserIdAsLong());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(alertService.getAlertList(authUserDto.getUserIdAsLong()));
    }
}
