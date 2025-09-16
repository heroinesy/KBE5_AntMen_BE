package com.antmen.antwork.common.service.serviceReservation;

import com.antmen.antwork.common.api.request.reservation.MatchingRequestDto;
import com.antmen.antwork.common.api.request.reservation.PaymentRequestDto;
import com.antmen.antwork.common.domain.entity.AlertTrigger;
import com.antmen.antwork.common.domain.entity.reservation.Matching;
import com.antmen.antwork.common.domain.entity.reservation.Payment;
import com.antmen.antwork.common.domain.entity.reservation.PaymentStatus;
import com.antmen.antwork.common.domain.entity.reservation.Reservation;
import com.antmen.antwork.common.domain.exception.NotFoundException;
import com.antmen.antwork.common.infra.repository.reservation.PaymentRepository;
import com.antmen.antwork.common.infra.repository.reservation.ReservationRepository;
import com.antmen.antwork.common.service.AlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final AlertService alertService;
    private final MatchingService matchingService;

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

        // ✅ 기존 방식 매칭 처리 시간 로그
        MatchingRequestDto matchingDto = MatchingRequestDto.from(reservation);
        long start = System.currentTimeMillis();
        log.info("📥 [동기] 매칭 처리 시작: reservationId={}", matchingDto.getReservationId());

        matchingService.createInitialMatchingFromDto(matchingDto);

        long end = System.currentTimeMillis();
        log.info("✅ [동기] 매칭 처리 완료: reservationId={}, 처리시간={}ms", matchingDto.getReservationId(), (end - start));

        // 알림
        if (!reservation.getMatchings().isEmpty()) {
            alertService.sendAlert(reservation.getMatchings().get(0).getManager().getUserId(),
                    AlertTrigger.MATCHING_REQUEST_TO_MANAGER,
                    reservation.getReservationId());
        } else {
            log.warn("❗ 매칭 리스트가 비어 있어 알림을 전송하지 않습니다. reservationId={}", reservation.getReservationId());
        }

        alertService.sendAlert(reservation.getCustomer().getUserId(),
                AlertTrigger.RESERVATION_CONFIRMED,
                reservation.getReservationId());

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