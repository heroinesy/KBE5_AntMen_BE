package com.antmen.antwork.admin.controller;

import com.antmen.antwork.admin.api.AdminRefundResponseDto;
import com.antmen.antwork.admin.service.AdminRefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/refunds")
public class AdminRefundController {
    private final AdminRefundService adminRefundService;

    @GetMapping
    public ResponseEntity<List<AdminRefundResponseDto>> getAllRefunds() {
        return ResponseEntity.ok(adminRefundService.getRefunds());
    }

    @GetMapping("/waiting")
    public ResponseEntity<List<AdminRefundResponseDto>> getWaitingRefunds() {
        return ResponseEntity.ok(adminRefundService.getWaitingRefunds());
    }

    @PutMapping("/{payId}/approve")
    public ResponseEntity<Void> approveRefund(@PathVariable Long payId) {
        adminRefundService.approveRefund(payId);
        return ResponseEntity.ok().build();
        // TODO: 결제 취소 처리 로직 추가
    }

    @PutMapping("/{payId}/reject")
    public ResponseEntity<Void> rejectRefund(@PathVariable Long payId) {
        adminRefundService.rejectRefund(payId);
        return ResponseEntity.ok().build();
    }
}