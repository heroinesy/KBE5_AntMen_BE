package com.antmen.antwork.manager.controller;

import com.antmen.antwork.common.api.request.ManagerSignupRequestDto;
import com.antmen.antwork.common.api.response.ManagerResponseDto;
import com.antmen.antwork.common.service.ManagerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/managers")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> signup(
            @Valid
            @ModelAttribute
            ManagerSignupRequestDto managerSignupRequestDto
            ) {
        try {
            return ResponseEntity.ok(managerService.signUp(managerSignupRequestDto));
        } catch (IOException e) {
            throw new RuntimeException("S3 업로드 중 오류가 발생했습니다."); // custom으로 수정 필요
        }
    }

    // 전체 조회
    @GetMapping
    public ResponseEntity<List<ManagerResponseDto>> getManagers() {
        return ResponseEntity.ok( managerService.getManagers());
    }

    // 1건 조회
    @GetMapping("/{id}")
    public ResponseEntity<ManagerResponseDto> getManager(@PathVariable Long id) {
        return ResponseEntity.ok(managerService.getManager(id));
    }



}
