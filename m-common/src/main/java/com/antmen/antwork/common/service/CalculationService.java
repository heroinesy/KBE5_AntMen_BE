package com.antmen.antwork.common.service;

import com.antmen.antwork.common.api.response.calculation.CalculationListWithTotalDto;
import com.antmen.antwork.common.api.response.calculation.CalculationResponseDto;
import com.antmen.antwork.common.domain.entity.account.User;
import com.antmen.antwork.common.domain.entity.reservation.Reservation;
import com.antmen.antwork.common.domain.entity.reservation.ReservationStatus;
import com.antmen.antwork.common.domain.exception.NotFoundException;
import com.antmen.antwork.common.infra.repository.account.UserRepository;
import com.antmen.antwork.common.infra.repository.reservation.ReservationRepository;
import com.antmen.antwork.common.service.mapper.CalculationMapper;
import com.antmen.antwork.common.service.mapper.reservation.ReservationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalculationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final CalculationMapper calculationMapper;

    @Transactional(readOnly = true)
    public List<CalculationResponseDto> getCalculationsById(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("유저가 존재하지 않습니다."));

        List<Reservation> list = reservationRepository.findByReservationStatusAndManager_UserId(ReservationStatus.DONE,userId);

        return list.stream()
                .map(calculationMapper::toDto).collect(Collectors.toList());
    }

    // 전체 정산내역
    @Transactional(readOnly = true)
    public List<CalculationResponseDto> getCalculations(Pageable pageable) {
        return reservationRepository.findByReservationStatus(ReservationStatus.DONE, pageable).stream()
                .map(calculationMapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CalculationListWithTotalDto getManagerCalculationsWithTotal(Long managerId, LocalDate weekStart, LocalDate weekEnd) {

        User user = userRepository.findById(managerId).orElseThrow(() -> new NotFoundException("유저가 존재하지 않습니다."));

        List<Reservation> reservations = reservationRepository.findByReservationStatusAndManager_UserIdAndReservationDateBetween(ReservationStatus.DONE, managerId, weekStart, weekEnd);

        List<CalculationResponseDto> list = reservations.stream()
                .map(calculationMapper::toDto)
                .collect(Collectors.toList());

        Integer total = reservations.stream()
                .mapToInt(Reservation::getReservationAmount)
                .sum();
        return new CalculationListWithTotalDto(list, total);
    }
}
