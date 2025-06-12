package com.antmen.antwork.admin.controller;

import com.antmen.antwork.common.api.response.reservation.RefundResponseDto;
import com.antmen.antwork.common.service.serviceReservation.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/refunds")
public class AdminRefundController {
    private final RefundService refundService;

    @GetMapping("/waiting")
    public ResponseEntity<List<RefundResponseDto>> getWaitingRefunds() {
        return ResponseEntity.ok(refundService.getWaitingRefunds());
    }

    @PutMapping("/{payId}/approve")
    public ResponseEntity<Void> approveRefund(@PathVariable Long payId) {
        refundService.approveRefund(payId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{payId}/reject")
    public ResponseEntity<Void> rejectRefund(@PathVariable Long payId) {
        refundService.rejectRefund(payId);
        return ResponseEntity.ok().build();
    }
}

