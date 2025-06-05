package com.antmen.antwork.common.service.serviceReservation;

import com.antmen.antwork.common.api.request.reservation.PaymentRequestDto;
import com.antmen.antwork.common.domain.entity.reservation.Payment;
import com.antmen.antwork.common.domain.entity.reservation.PaymentStatus;
import com.antmen.antwork.common.infra.repository.reservation.PaymentRepository;
import com.antmen.antwork.common.infra.repository.reservation.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;

    @Override
    @Transactional
    public ResponseEntity<String> requestPayment(PaymentRequestDto requestDto) {
        if (requestDto.getReservationId() == null) {
            throw new IllegalArgumentException("reservationId는 필수입니다.");
        }
        if (requestDto.getPayMethod() == null || requestDto.getPayMethod().isBlank()) {
            throw new IllegalArgumentException("payMethod는 필수입니다.");
        }
        if (requestDto.getPayAmount() <= 0) {
            throw new IllegalArgumentException("payAmount는 필수입니다.");
        }
        
        try {
            // 1. 결제 정보 저장
            Payment payment = savePaymentInfo(requestDto);
            
            // 2. 결제 상태 업데이트
            updatePaymentStatus(payment, PaymentStatus.REQUESTED);

            return ResponseEntity.ok("결제 요청이 성공적으로 저장되었습니다. paymentId: " );
        } catch (Exception e) {
            e.printStackTrace(); 
            String errorMsg = "결제 요청 처리 중 에러 발생: " + e.getMessage();
            return ResponseEntity.status(500).body(errorMsg);
        }
    }

    @Override
    @Transactional
    public Payment savePaymentInfo(PaymentRequestDto requestDto) {
        Payment payment = Payment.builder()
                .reservation(reservationRepository.findById(requestDto.getReservationId()).get())
                .payMethod(requestDto.getPayMethod())
                .payAmount(requestDto.getPayAmount())
                .payStatus(PaymentStatus.READY)
                .payRequestTime(LocalDateTime.now())
                .build();
        
        return paymentRepository.save(payment);
    }

    @Override
    @Transactional
    public void updatePaymentStatus(Payment payment, PaymentStatus status) {
        payment.setPayStatus(status);
        paymentRepository.save(payment);
    }

    @Override
    public Payment getPaymentInfo(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
    }
}