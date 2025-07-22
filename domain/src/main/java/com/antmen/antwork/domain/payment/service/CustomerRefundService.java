package com.antmen.antwork.domain.payment.service;















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