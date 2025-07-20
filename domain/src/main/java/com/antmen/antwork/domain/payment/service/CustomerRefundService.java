package com.antmen.antwork.domain.payment.service;

import com.antmen.antwork.domain.payment.entity.Payment;
import com.antmen.antwork.domain.payment.entity.Refund;
import com.antmen.antwork.domain.payment.entity.RefundStatus;
import com.antmen.antwork.common.domain.entity.reservation.Reservation;
import com.antmen.antwork.common.domain.exception.NotFoundException;
import com.antmen.antwork.domain.payment.repository.PaymentRepository;
import com.antmen.antwork.domain.payment.repository.RefundRepository;
import com.antmen.antwork.common.infra.repository.reservation.ReservationRepository;
import com.antmen.antwork.customer.api.request.CustomerRefundRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerRefundService {
    private final RefundRepository refundRepository;
    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    @CacheEvict(value = {"refundReasonStatistics", "topRefundReasons", "autoRefundDetails"}, allEntries = true)
    public void requestRefund(CustomerRefundRequestDto requestDto) {
        Reservation reservation = reservationRepository.findById(requestDto.getReservationId())
                .orElseThrow(() -> new NotFoundException("예약 정보를 찾을 수 없습니다."));

        Payment payment = paymentRepository.findByReservation(reservation)
                .orElseThrow(() -> new NotFoundException("해당 예약의 결제 정보를 찾을 수 없습니다."));

        if (refundRepository.existsByPayment_PayId(payment.getPayId())) {
            throw new IllegalArgumentException("이미 환불 요청이 존재합니다.");
        }

        Refund refund = Refund.builder()
                .payment(payment)
                .refundReason(requestDto.getRefundReason())
                .refundAmount(requestDto.getRefundAmount())
                .refundStatus(RefundStatus.WAITING)
                .build();

        refundRepository.save(refund);
    }

    // TODO: 본인 환불 내역 가져오기
    // CustomerRefundResponseDto
}