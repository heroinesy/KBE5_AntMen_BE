package com.antmen.antwork.domain.reservation.service;

import com.antmen.antwork.domain.category.entity.CategoryOption;
import com.antmen.antwork.domain.category.repository.CategoryOptionRepository;
import com.antmen.antwork.domain.reservation.dto.ReservationOptionResponseDto;
import com.antmen.antwork.domain.reservation.entity.Reservation;
import com.antmen.antwork.domain.reservation.entity.ReservationOption;
import com.antmen.antwork.domain.reservation.mapper.ReservationOptionMapper;
import com.antmen.antwork.domain.reservation.repository.ReservationOptionRepository;
import com.antmen.antwork.domain.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationOptionService {

    private final ReservationRepository reservationRepository;
    private final ReservationOptionRepository reservationOptionRepository;
    private final CategoryOptionRepository categoryOptionRepository;
    private final ReservationOptionMapper reservationOptionMapper;

    /**
     * 예약 옵션 저장
     */
    @Transactional
    public void saveReservationOptions(Long reservationId, List<Long> categoryOptionIds){
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(()-> new IllegalArgumentException("예약을 찾을 수 없습니다."));

        List<CategoryOption> options = categoryOptionRepository.findAllById(categoryOptionIds);
        if (options.size() != categoryOptionIds.size()) {
            throw new IllegalArgumentException("유효하지 않은 옵션 ID가 포함되어 있습니다.");
        }
        List<ReservationOption> entities = options.stream()
                .map(option -> reservationOptionMapper.toEntity(reservation, option))
                .collect(Collectors.toList());

        reservationOptionRepository.saveAll(entities);
    }

    /**
     * 예약 ID로 옵션 ID 리스트 반환
     */
    @Transactional(readOnly = true)
    public List<ReservationOptionResponseDto> getReservationOptionDtos(Long reservationId) {
        List<ReservationOption> entities = reservationOptionRepository.findByReservation_ReservationId(reservationId);
        return reservationOptionMapper.toResponseDtoList(entities);
    }
}