package com.antmen.antwork.domain.reservation.service;

















@Service
@RequiredArgsConstructor
public class ReservationCommentService {
    private final ReservationRepository reservationRepository;
    private final ReservationCommentRepository reservationCommentRepository;
    private final ReservationMapper reservationMapper;
    private final AlertService alertService;

    // 매니저 check-in time update
    @Transactional
    public void checkIn(Long reservationId){
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("예약을 찾을 수 없습니다."));

        ReservationComment comment = reservationCommentRepository.findById(reservationId)
                .orElse(ReservationComment.builder()
                        .reservation(reservation).build());

        comment.setCheckinAt(LocalDateTime.now());
        reservationCommentRepository.save(comment);

        alertService.sendAlert(reservation.getCustomer().getUserId(), AlertTrigger.SERVICE_CHECK_IN,reservation.getReservationId());

    }

    // 매니저 check-out time update
    @Transactional
    public void checkOut(Long reservationId, CheckOutRequestDto dto) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("예약이 존재하지 않습니다."));
        reservation.setReservationStatus(ReservationStatus.DONE);


        ReservationComment comment = reservationCommentRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("체크인 기록이 없습니다."));

        comment.setCheckoutAt(LocalDateTime.now());
        comment.setComment(dto.getComment());
        reservation.getManager().setLastReservationAt(LocalDateTime.now());
        reservationCommentRepository.save(comment);

        alertService.sendAlert(reservation.getCustomer().getUserId(), AlertTrigger.SERVICE_CHECK_OUT,reservation.getReservationId());

    }

    @Transactional(readOnly = true)
    public ReservationCommentResponseDto getComment(Long reservationId) {
        ReservationComment comment = reservationCommentRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("예약 댓글 정보가 없습니다."));
        return reservationMapper.toDto(comment);
    }

}