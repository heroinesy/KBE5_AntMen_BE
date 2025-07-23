package com.antmen.antwork.common.service.serviceReservation;

import com.antmen.antwork.common.api.response.reservation.ReservationOptionResponseDto;
import com.antmen.antwork.common.domain.entity.reservation.CategoryOption;
import com.antmen.antwork.common.domain.entity.reservation.Reservation;
import com.antmen.antwork.common.domain.entity.reservation.ReservationOption;
import com.antmen.antwork.common.infra.repository.reservation.CategoryOptionRepository;
import com.antmen.antwork.common.infra.repository.reservation.ReservationOptionRepository;
import com.antmen.antwork.common.infra.repository.reservation.ReservationRepository;
import com.antmen.antwork.common.service.mapper.reservation.ReservationOptionMapper;
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
