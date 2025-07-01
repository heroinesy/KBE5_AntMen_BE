package com.antmen.antwork.common.service.serviceReservation;

import com.antmen.antwork.common.api.request.reservation.PaymentRequestDto;
import com.antmen.antwork.common.domain.entity.reservation.Payment;
import com.antmen.antwork.common.domain.entity.reservation.PaymentStatus;
import com.antmen.antwork.common.domain.entity.reservation.Reservation;
import com.antmen.antwork.common.domain.exception.NotFoundException;
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

        Reservation reservation = reservationRepository.findById(requestDto.getReservationId())
                .orElseThrow(() -> new NotFoundException("해당 예약이 존재하지 않습니다"));

        if (!reservation.getReservationAmount().equals(requestDto.getPayAmount())) {
            throw new IllegalArgumentException("결제 금액이 예약 금액과 일치하지 않습니다.");
        }
        Payment payment = savePaymentInfo(requestDto);
        updatePaymentStatus(payment, PaymentStatus.DONE);

        String response = String.format(
                "{\"message\": \"결제 요청이 성공적으로 저장되었습니다.\", \"paymentId\": %d}",
                payment.getPayId()
        );

        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(response);
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