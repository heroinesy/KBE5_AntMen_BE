package com.antmen.antwork.admin.service;

import com.antmen.antwork.admin.api.AdminRefundResponseDto;
import com.antmen.antwork.common.domain.entity.reservation.Payment;
import com.antmen.antwork.common.domain.entity.reservation.PaymentStatus;
import com.antmen.antwork.common.domain.entity.reservation.Refund;
import com.antmen.antwork.common.domain.entity.reservation.RefundStatus;
import com.antmen.antwork.common.domain.exception.NotFoundException;
import com.antmen.antwork.common.infra.repository.reservation.RefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminRefundService {
    private final RefundRepository refundRepository;

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
    public void approveRefund(Long payId) {
        Refund refund = refundRepository.findById(payId)
                .orElseThrow(() -> new NotFoundException("해당 환불 내역이 존재하지 않습니다."));
        refund.setRefundStatus(RefundStatus.APPROVED);
        refund.setRefundProcessedAt(LocalDateTime.now());

        Payment payment = refund.getPayment();
        payment.setPayStatus(PaymentStatus.CANCELED);
    }

    @Transactional
    public void rejectRefund(Long payId) {
        Refund refund = refundRepository.findById(payId)
                .orElseThrow(() -> new NotFoundException("해당 환불 내역이 존재하지 않습니다."));
        refund.setRefundStatus(RefundStatus.REJECTED);
        refund.setRefundProcessedAt(LocalDateTime.now());
    }
}