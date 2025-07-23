package com.antmen.antwork.manager.controller;

import com.antmen.antwork.common.api.request.account.CustomerUpdateRequest;
import com.antmen.antwork.common.api.request.account.ManagerInfoUpdateRequestDto;
import com.antmen.antwork.common.api.request.account.ManagerSignupRequestDto;
import com.antmen.antwork.common.api.request.account.ManagerUpdateRequestDto;
import com.antmen.antwork.common.api.response.account.CustomerProfileResponse;
import com.antmen.antwork.common.api.response.account.ManagerResponseDto;
import com.antmen.antwork.common.service.serviceAccount.ManagerService;
import com.antmen.antwork.common.util.AuthUserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> signup(
            @Valid @ModelAttribute ManagerSignupRequestDto managerSignupRequestDto) {
        try {
            return ResponseEntity.ok(managerService.signUp(managerSignupRequestDto));
        } catch (IOException e) {
            throw new RuntimeException("S3 업로드 중 오류가 발생했습니다."); // custom으로 수정 필요
        }
    }

    // 내정보조회
    @GetMapping("/me")
    public ResponseEntity<ManagerResponseDto> getMyInfo(
            @AuthenticationPrincipal AuthUserDto authUserDto) {
        return ResponseEntity.ok(managerService.getMyInfo(authUserDto.getUserIdAsLong()));
    }

    // 내정보 수정
    @PutMapping("/me")
    public ResponseEntity<ManagerResponseDto> updateMyInfo(
            @AuthenticationPrincipal AuthUserDto authUserDto,
            @RequestBody @Valid ManagerInfoUpdateRequestDto managerInfoUpdateRequestDto ) {
        ManagerResponseDto response = managerService.updateMyInfo(authUserDto.getUserIdAsLong(), managerInfoUpdateRequestDto);

        return ResponseEntity.ok(response);

    }

    // 프로필만 수정
    @PutMapping("/me/profile")
    public ResponseEntity<Map<String, String>> updateProfile(
            @AuthenticationPrincipal AuthUserDto authUserDto,
            @RequestParam MultipartFile userProfile
    ) {
        try {
            managerService.updateProfileImage(authUserDto.getUserIdAsLong(), userProfile);
            return ResponseEntity.ok(Map.of("message", "프로필 수정 완료"));
        } catch (IOException e) {
            throw new RuntimeException("S3 업로드 중 오류가 발생했습니다.");
        }
    }

    // 거절 후 재요청
    @PutMapping(value = "/reapply", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> reapplyManager(
            @AuthenticationPrincipal AuthUserDto authUserDto,
            @ModelAttribute ManagerUpdateRequestDto managerUpdateRequestDto
    ){
        try {
            managerService.reapplyManager(authUserDto.getUserIdAsLong(), managerUpdateRequestDto);

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch ( IOException e ) {
            throw new RuntimeException("S3 업로드 중 오류가 발생했습니다.");
        }
    }

    // 거절 사유 조회
    @GetMapping("/{managerId}/reject-reason")
    public ResponseEntity<String> getRejectReason(@PathVariable Long managerId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(managerService.getRejectReason(managerId));
    }

    // 전체 조회
    @GetMapping
    public ResponseEntity<List<ManagerResponseDto>> getManagers() {
        return ResponseEntity.ok(managerService.getManagers());
    }

    // 1건 조회
    @GetMapping("/{id}")
    public ResponseEntity<ManagerResponseDto> getManager(@PathVariable Long id) {
        return ResponseEntity.ok(managerService.getManager(id));
    }

}
