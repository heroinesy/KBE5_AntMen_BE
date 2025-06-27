package com.antmen.antwork.common.service.serviceReservation;

import com.antmen.antwork.common.api.request.reservation.CheckInRequestDto;
import com.antmen.antwork.common.api.request.reservation.CheckOutRequestDto;
import com.antmen.antwork.common.api.response.reservation.ReservationCommentResponseDto;
import com.antmen.antwork.common.domain.entity.reservation.Reservation;
import com.antmen.antwork.common.domain.entity.reservation.ReservationComment;
import com.antmen.antwork.common.domain.entity.reservation.ReservationStatus;
import com.antmen.antwork.common.domain.exception.NotFoundException;
import com.antmen.antwork.common.infra.repository.reservation.ReservationCommentRepository;
import com.antmen.antwork.common.infra.repository.reservation.ReservationRepository;
import com.antmen.antwork.common.service.mapper.reservation.ReservationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReservationCommentService {
    private final ReservationRepository reservationRepository;
    private final ReservationCommentRepository reservationCommentRepository;
    private final ReservationMapper reservationMapper;

    // 매니저 check-in time update
    @Transactional
    public void checkIn(Long reservationId, CheckInRequestDto dto){
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("예약을 찾을 수 없습니다."));

        ReservationComment comment = reservationCommentRepository.findById(reservationId)
                .orElse(ReservationComment.builder()
                        .reservation(reservation).build());

        comment.setCheckinAt(dto.getCheckinAt());
        reservationCommentRepository.save(comment);
    }

    // 매니저 check-out time update
    @Transactional
    public void checkOut(Long reservationId, CheckOutRequestDto dto) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("예약이 존재하지 않습니다."));
        reservation.setReservationStatus(ReservationStatus.DONE);


        ReservationComment comment = reservationCommentRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("체크인 기록이 없습니다."));

        comment.setCheckoutAt(dto.getCheckoutAt());
        comment.setComment(dto.getComment());
        reservation.getManager().setLastReservationAt(LocalDateTime.now());
        reservationCommentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public ReservationCommentResponseDto getComment(Long reservationId) {
        ReservationComment comment = reservationCommentRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("예약 댓글 정보가 없습니다."));
        return reservationMapper.toDto(comment);
    }

}