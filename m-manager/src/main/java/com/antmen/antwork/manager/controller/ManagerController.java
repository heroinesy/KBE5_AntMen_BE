package com.antmen.antwork.manager.controller;

import com.antmen.antwork.common.api.request.account.CustomerUpdateRequest;
import com.antmen.antwork.common.api.request.account.ManagerSignupRequestDto;
import com.antmen.antwork.common.api.request.account.ManagerUpdateRequestDto;
import com.antmen.antwork.common.api.response.account.CustomerProfileResponse;
import com.antmen.antwork.common.api.response.account.ManagerResponseDto;
import com.antmen.antwork.common.service.serviceAccount.ManagerService;
import com.antmen.antwork.common.util.AuthUserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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
            @RequestBody @Valid ManagerUpdateRequestDto managerUpdateRequestDto ) {
        ManagerResponseDto response = managerService.updateMyInfo(authUserDto.getUserIdAsLong(), managerUpdateRequestDto);

        return ResponseEntity.ok(response);

    }

    // 거절 후 재요청
    @PostMapping(value = "/reapply", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ManagerResponseDto> reapplyManager(
            @AuthenticationPrincipal AuthUserDto authUserDto,
            @ModelAttribute ManagerUpdateRequestDto managerUpdateRequestDto
    ){
        try {
            return ResponseEntity.ok(managerService.reapplyManager(authUserDto.getUserIdAsLong(), managerUpdateRequestDto));
        } catch ( IOException e ) {
            throw new RuntimeException("S3 업로드 중 오류가 발생했습니다.");
        }
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
