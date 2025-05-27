package com.antmen.antwork.common.service;

import com.antmen.antwork.common.api.request.ReservationRequestDto;
import com.antmen.antwork.common.api.response.ReservationResponseDto;
import com.antmen.antwork.common.domain.entity.Reservation;
import com.antmen.antwork.common.infra.repository.ReservationRepository;
import com.antmen.antwork.common.service.mapper.ReservationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;

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

    @Transactional(readOnly = true)
    public ReservationResponseDto getReservation(Long id) {
        try {
            Reservation reservation = reservationRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));
            return reservationMapper.toDto(reservation);
        } catch (Exception e) {
            throw new RuntimeException("예약 조회 중 오류가 발생했습니다.", e);
        }
    }
}
