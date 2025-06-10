package com.antmen.antwork.common.service.serviceReservation;

import com.antmen.antwork.common.api.response.reservation.RefundResponseDto;
import com.antmen.antwork.common.domain.entity.reservation.Refund;
import com.antmen.antwork.common.domain.entity.reservation.RefundStatus;
import com.antmen.antwork.common.domain.exception.NotFoundException;
import com.antmen.antwork.common.infra.repository.reservation.RefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RefundService {
    private final RefundRepository refundRepository;

    public List<RefundResponseDto> getWaitingRefunds() {
        return refundRepository.findByRefundStatus(RefundStatus.WAITING).stream()
                .map(RefundResponseDto::from)
                .collect(Collectors.toList());
    }

    public void approveRefund(Long payId) {
        Refund refund = refundRepository.findById(payId)
                .orElseThrow(() -> new NotFoundException("해당 환불 내역이 존재하지 않습니다."));
        refund.setRefundStatus(RefundStatus.APPROVED);
        refund.setRefundProcessedAt(LocalDateTime.now());
    }

    public void rejectRefund(Long payId) {
        Refund refund = refundRepository.findById(payId)
                .orElseThrow(() -> new NotFoundException("해당 환불 내역이 존재하지 않습니다."));
        refund.setRefundStatus(RefundStatus.REJECTED);
        refund.setRefundProcessedAt(LocalDateTime.now());
    }
}
