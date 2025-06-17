package com.antmen.antwork.customer.controller;

import com.antmen.antwork.common.api.request.reservation.PaymentRequestDto;
import com.antmen.antwork.common.domain.entity.reservation.Payment;
import com.antmen.antwork.common.service.serviceReservation.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // 결제 요청
    @PostMapping("/request")
    public ResponseEntity<String> requestPayment(@Valid @RequestBody PaymentRequestDto requestDto) {
        return paymentService.requestPayment(requestDto);
    }

    // 결제 정보 조회
    @GetMapping("/id/{paymentId}")
    public Payment getPaymentInfo(@PathVariable Long paymentId) {
        return paymentService.getPaymentInfo(paymentId);
    }

    // 결제 성공 콜백
    @GetMapping("/success")
    public String paymentSuccess(
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam Integer amount) {
        // TODO: 결제 성공 처리 로직 구현
        return "결제가 성공적으로 완료되었습니다.";
    }

    // 결제 실패 콜백
    @GetMapping("/fail")
    public String paymentFail(
            @RequestParam String code,
            @RequestParam String message,
            @RequestParam String orderId) {
        // TODO: 결제 실패 처리 로직 구현
        return "결제가 실패했습니다. 사유: " + message;
    }
}