package com.antmen.antwork.api.admin;

import com.antmen.antwork.domain.user.dto.*;
import com.antmen.antwork.domain.user.entity.User;
import com.antmen.antwork.domain.user.service.AdminUserService;
import com.antmen.antwork.domain.user.service.ManagerService;
import com.antmen.antwork.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/users")
public class AdminUserController {
    private final UserService userService;
    private final ManagerService managerService;
    private final AdminUserService adminUserService;

    /**
     * 고객 목록 조회 (페이징 지원)
     */
    @GetMapping("/customers")
    public ResponseEntity<Page<UserListResponseDto>> searchCustomers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String sortBy, // 정렬 기준: "joinDate", "lastReservation"
            @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<UserListResponseDto> customers = userService.searchCustomers(name, sortBy, pageable);
        return ResponseEntity.ok(customers);
    }

    /**
     * 승인된 매니저 목록 조회 (페이징 지원)
     */
    @GetMapping("/managers")
    public ResponseEntity<Page<UserListResponseDto>> searchApprovedManagers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String sortBy, // 정렬 기준: "joinDate", "lastReservation"
            @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<UserListResponseDto> managers = managerService.searchApprovedManagers(name, sortBy, pageable);
        return ResponseEntity.ok(managers);
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
    public ResponseEntity<Page<ManagerWatingListDto>> getWaitingManagers(
            @RequestParam(required = false) String name,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(managerService.getWaitingManagers(name, pageable));
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

    /**
     * 블랙리스트 회원 목록 조회
     */
    @GetMapping("/blacklist")
    public ResponseEntity<Page<UserListResponseDto>> getBlacklistUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String userRole,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<UserListResponseDto> blacklistUsers = userService.getBlacklistUsers(name, userRole, pageable);
        return ResponseEntity.ok(blacklistUsers);
    }

    /**
     * 회원을 블랙리스트에 추가
     */
    @PostMapping("/{userId}/blacklist")
    public ResponseEntity<Void> addToBlacklist(
            @PathVariable Long userId,
            @RequestParam String reason
    ) {
        userService.addToBlacklist(userId, reason);
        return ResponseEntity.ok().build();
    }

    /**
     * 회원을 블랙리스트에서 제거
     */
    @DeleteMapping("/{userId}/blacklist")
    public ResponseEntity<Void> removeFromBlacklist(@PathVariable Long userId) {
        userService.removeFromBlacklist(userId);
        return ResponseEntity.ok().build();
    }

    /**
     * 수요자 상세정보 통합 조회 (기본정보 + 예약통계 + 리뷰정보)
     */
    @GetMapping("/customers/{userId}/detail")
    public ResponseEntity<CustomerDetailResponseDto> getCustomerDetail(@PathVariable Long userId) {
        return ResponseEntity.ok(adminUserService.getCustomerDetail(userId));
    }

    /**
     * 매니저 상세정보 통합 조회 (기본정보 + 매칭통계 + 근무내역)
     */
    @GetMapping("/managers/{userId}/detail")
    public ResponseEntity<ManagerDetailResponseDto> getManagerDetail(@PathVariable Long userId) {
        return ResponseEntity.ok(adminUserService.getManagerDetail(userId));
    }
}