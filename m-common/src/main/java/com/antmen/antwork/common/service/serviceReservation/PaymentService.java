package com.antmen.antwork.common.service.serviceReservation;

import com.antmen.antwork.common.api.request.reservation.PaymentRequestDto;
import com.antmen.antwork.common.domain.entity.reservation.Payment;
import com.antmen.antwork.common.domain.entity.reservation.PaymentStatus;
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