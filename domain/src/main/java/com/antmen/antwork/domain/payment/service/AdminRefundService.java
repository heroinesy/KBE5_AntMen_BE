package com.antmen.antwork.domain.payment.service;

import com.antmen.antwork.domain.payment.dto.AdminRefundResponseDto;
import com.antmen.antwork.domain.payment.entity.Payment;
import com.antmen.antwork.domain.payment.entity.PaymentStatus;
import com.antmen.antwork.domain.payment.entity.Refund;
import com.antmen.antwork.domain.payment.entity.RefundStatus;
import com.antmen.antwork.common.domain.exception.NotFoundException;
import com.antmen.antwork.domain.payment.repository.PaymentRepository;
import com.antmen.antwork.domain.payment.repository.RefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminRefundService {
    private final RefundRepository refundRepository;
    private final PaymentRepository paymentRepository;

    public List<AdminRefundResponseDto> getRefunds() {
        return refundRepository.findAll().stream()
                .map(AdminRefundResponseDto::from)
                .collect(Collectors.toList());
    }

    public List<AdminRefundResponseDto> getWaitingRefunds() {
        return refundRepository.findByRefundStatus(RefundStatus.WAITING).stream()
                .map(AdminRefundResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    @CacheEvict(value = {"refundReasonStatistics", "topRefundReasons", "autoRefundDetails"}, allEntries = true)
    public void approveRefund(Long payId) {
        Refund refund = refundRepository.findById(payId)
                .orElseThrow(() -> new NotFoundException("해당 환불 내역이 존재하지 않습니다."));
        refund.setRefundStatus(RefundStatus.APPROVED);
        refund.setRefundProcessedAt(LocalDateTime.now());

        Payment payment = refund.getPayment();
        payment.setPayStatus(PaymentStatus.CANCELED);
    }

    @Transactional
    @CacheEvict(value = {"refundReasonStatistics", "topRefundReasons", "autoRefundDetails"}, allEntries = true)
    public void rejectRefund(Long payId) {
        Refund refund = refundRepository.findById(payId)
                .orElseThrow(() -> new NotFoundException("해당 환불 내역이 존재하지 않습니다."));
        refund.setRefundStatus(RefundStatus.REJECTED);
        refund.setRefundProcessedAt(LocalDateTime.now());
    }

    // 자동환불
    @Transactional
    @CacheEvict(value = {"refundReasonStatistics", "topRefundReasons", "autoRefundDetails"}, allEntries = true)
    public void autoRefund(Long reservationId, String reason) {
        Payment payment = paymentRepository.findByReservation_ReservationId(reservationId);

        // 환불 테이블 저장
        Refund refund = Refund.builder()
                .payment(payment)
                .refundCreatedAt(LocalDateTime.now())
                .refundReason(reason)
                .refundAmount(payment.getPayAmount())
                .refundStatus(RefundStatus.APPROVED)
                .refundProcessedAt(LocalDateTime.now())
                .build();
        refundRepository.save(refund);

        // 결제 상태 변경
        payment.setPayStatus(PaymentStatus.CANCELED);
        paymentRepository.save(payment);
    }
}