package com.antmen.antwork.admin.controller;

import com.antmen.antwork.domain.user.dto.AdminPasswordChangeDto;
import com.antmen.antwork.domain.user.dto.UserLoginDto;
import com.antmen.antwork.domain.user.dto.AdminTokenDto;
import com.antmen.antwork.domain.user.service.AdminService;
import com.antmen.antwork.common.util.AuthUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/auth")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;


    @PostMapping("/login")
    public ResponseEntity<AdminTokenDto> login(
            @RequestBody UserLoginDto userLoginDto
            ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(adminService.login(userLoginDto));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @RequestBody AdminPasswordChangeDto adminPasswordChangeDto,
            @AuthenticationPrincipal AuthUserDto authUserDto
            ){

        Long adminId = authUserDto.getUserIdAsLong();
        adminService.changePassword(adminId, adminPasswordChangeDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
