package com.antmen.antwork.domain.payment.service;

import com.antmen.antwork.domain.payment.dto.PaymentRequestDto;
import com.antmen.antwork.domain.payment.entity.Payment;
import com.antmen.antwork.domain.payment.entity.PaymentStatus;
import org.springframework.http.ResponseEntity;

public interface PaymentService {
    // 결제 요청 처리
    ResponseEntity<String> requestPayment(PaymentRequestDto requestDto);
    
    // 결제 정보 저장
    Payment savePaymentInfo(PaymentRequestDto requestDto);
    
    // 결제 상태 업데이트
    void updatePaymentStatus(Payment payment, PaymentStatus status);
    
    // 결제 정보 조회
    Payment getPaymentInfo(Long paymentId);
}