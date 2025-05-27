package com.antmen.antwork.common.service;

import com.antmen.antwork.common.domain.entity.Reservation;
import com.antmen.antwork.common.domain.exception.NotFoundException;
import com.antmen.antwork.common.infra.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public void changeReservationStatus(Long id, String newStatus) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 예약이 존재하지 않습니다."));
        reservation.setReservationStatus(newStatus);
    }


    /**
     * 예약 취소 처리
     */
    public void cancelReservation(Long id, String cancelReason) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 예약이 존재하지 않습니다."));
        reservation.setReservationStatus("C");
        reservation.setReservationCancelReason(cancelReason);
    }
}