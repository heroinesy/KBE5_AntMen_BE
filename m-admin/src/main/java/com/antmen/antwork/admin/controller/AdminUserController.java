package com.antmen.antwork.admin.controller;

import com.antmen.antwork.common.api.response.account.ManagerResponseDto;
import com.antmen.antwork.common.api.response.account.ManagerWatingListDto;
import com.antmen.antwork.common.api.response.account.UserResponseDto;
import com.antmen.antwork.common.domain.entity.account.User;
import com.antmen.antwork.common.domain.entity.account.UserRole;
import com.antmen.antwork.common.service.serviceAccount.ManagerService;
import com.antmen.antwork.common.service.serviceAccount.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/users")
public class AdminUserController {
    private final UserService userService;
    private final ManagerService managerService;

    /**
     * 회원 목록 조회
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> searchUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) UserRole role
    ) {
        List<User> users = userService.searchUsers(name, userId, role);
        List<UserResponseDto> result = users.stream()
                .map(UserResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    /**
     * 회원 단건 조회
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(UserResponseDto.from(user));
    }

    /**
     * 승인 대기 중인 매니저 조회
     */
    @GetMapping("/waiting-managers")
    public ResponseEntity<List<ManagerWatingListDto>> getWaitingManagers() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(managerService.getWaitingManagers());
    }

    @GetMapping("/waiting-managers/{userId}")
    public ResponseEntity<ManagerResponseDto> getWaitingManager(@PathVariable Long userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(managerService.getWaitingManagerDetail(userId));
    }

    /**
     * 매니저 가입 승인
     */
    @PostMapping("/{userId}/approve")
    public ResponseEntity<Void> approveUser(@PathVariable Long userId) {
        managerService.approveManager(userId);
        return ResponseEntity.ok().build();
    }

    /**
     * 매니저 가입 거절
     */
    @PostMapping("/{userId}/reject")
    public ResponseEntity<Void> rejectManager(
            @PathVariable Long userId,
            @RequestParam String reason
    ) {
        managerService.rejectManager(userId, reason);
        return ResponseEntity.ok().build();
    }
}