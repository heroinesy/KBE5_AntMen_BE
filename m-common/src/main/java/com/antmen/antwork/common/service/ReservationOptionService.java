package com.antmen.antwork.common.service;

import com.antmen.antwork.common.api.response.ReservationOptionResponseDto;
import com.antmen.antwork.common.domain.entity.CategoryOption;
import com.antmen.antwork.common.domain.entity.Reservation;
import com.antmen.antwork.common.domain.entity.ReservationOption;
import com.antmen.antwork.common.infra.repository.CategoryOptionRepository;
import com.antmen.antwork.common.infra.repository.ReservationOptionRepository;
import com.antmen.antwork.common.infra.repository.ReservationRepository;
import com.antmen.antwork.common.service.mapper.ReservationOptionMapper;
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

        List<ReservationOption> options = categoryOptionIds.stream()
                .map(coId -> {
                    CategoryOption co = categoryOptionRepository.findById(coId)
                            .orElseThrow(() -> new IllegalArgumentException("옵션을 찾을 수 없습니다. coId=" + coId));
                    return reservationOptionMapper.toEntity(reservation, co);
                })
                .collect(Collectors.toList());

        reservationOptionRepository.saveAll(options);
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
