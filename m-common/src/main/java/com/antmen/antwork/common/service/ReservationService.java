package com.antmen.antwork.common.service;

import com.antmen.antwork.common.api.request.ReservationRequestDto;
import com.antmen.antwork.common.api.response.ReservationResponseDto;
import com.antmen.antwork.common.domain.constant.ReservationConstants;
import com.antmen.antwork.common.domain.entity.Reservation;
import com.antmen.antwork.common.domain.exception.NotFoundException;
import com.antmen.antwork.common.infra.repository.ReservationRepository;
import com.antmen.antwork.common.service.mapper.ReservationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;

    private static final Set<String> VALID_STATUS = Set.of(
            ReservationConstants.STATUS_WAITING,
            ReservationConstants.STATUS_MATCHING,
            ReservationConstants.STATUS_PAY,
            ReservationConstants.STATUS_DONE,
            ReservationConstants.STATUS_CANCEL,
            ReservationConstants.STATUS_ERROR
    );

    @Transactional
    public ReservationResponseDto createReservation(ReservationRequestDto requestDto) {
        try {
            Reservation reservation = reservationMapper.toEntity(requestDto);
            Reservation saved = reservationRepository.save(reservation);
            return reservationMapper.toDto(saved);
        } catch (Exception e) {
            throw new RuntimeException("예약 생성 중 오류가 발생했습니다.", e);
        }
    }


    public void changeReservationStatus__(Long id, String newStatus) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 예약이 존재하지 않습니다."));
        reservation.setReservationStatus(newStatus);
    }


    /**
     * 예약 취소 처리
     */
    public void cancelReservation__(Long id, String cancelReason) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 예약이 존재하지 않습니다."));
        reservation.setReservationStatus("C");
        reservation.setReservationCancelReason(cancelReason);
    }

    @Transactional
    public void changeStatus(Long id, String status) {
        if (!VALID_STATUS.contains(status)) {
            throw new IllegalArgumentException("유효하지 않은 예약 상태입니다.");
        }
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));
        reservation.setReservationStatus(status);
        reservationRepository.save(reservation);
    }

    @Transactional
    public void cancelReservation(Long id, String cancelReason) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));
        reservation.setReservationStatus(ReservationConstants.STATUS_CANCEL);
        reservation.setReservationCancelReason(cancelReason);
        reservationRepository.save(reservation);
    }
}
