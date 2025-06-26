package com.antmen.antwork.common.service;

import com.antmen.antwork.common.api.response.calculation.CalculationListWithTotalDto;
import com.antmen.antwork.common.api.response.calculation.CalculationResponseDto;
import com.antmen.antwork.common.domain.entity.account.User;
import com.antmen.antwork.common.domain.entity.account.UserRole;
import com.antmen.antwork.common.domain.entity.reservation.Calculation;
import com.antmen.antwork.common.domain.entity.reservation.CalculationStatus;
import com.antmen.antwork.common.domain.entity.reservation.Reservation;
import com.antmen.antwork.common.domain.entity.reservation.ReservationStatus;
import com.antmen.antwork.common.domain.exception.NotFoundException;
import com.antmen.antwork.common.domain.exception.UnauthorizedAccessException;
import com.antmen.antwork.common.infra.repository.account.UserRepository;
import com.antmen.antwork.common.infra.repository.reservation.CalculationRepository;
import com.antmen.antwork.common.infra.repository.reservation.ReservationRepository;
import com.antmen.antwork.common.service.mapper.CalculationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalculationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final CalculationMapper calculationMapper;
    private final CalculationRepository calculationRepository;

    /**
     * 상태가 done인 예약 조회
     * 정산에 포함된 예약도 포함되므로 정산 요청 로직에는 사용 X
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public List<CalculationResponseDto> getCalculationsById(Long userId) {

        User user = userRepository.findById(userId)
                .filter(u -> u.getUserRole() == UserRole.MANAGER)
                .orElseThrow(() -> new NotFoundException("매니저를 찾을 수 없습니다."));

        List<Reservation> list = reservationRepository.findByReservationStatusAndManager_UserId(ReservationStatus.DONE,userId);

        return list.stream()
                .map(calculationMapper::toDto).collect(Collectors.toList());
    }

    // 전체 정산내역
    @Transactional(readOnly = true)
    public Page<CalculationResponseDto> getCalculations(Pageable pageable) {
        return reservationRepository.findByReservationStatus(ReservationStatus.DONE, pageable)
                .map(calculationMapper::toDto);
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

    /**
     * 이전 정산 내역 조회
     * @param userId 매니저 ID
     * @return CalculationResponseDto 리스트
     */
    @Transactional(readOnly = true)
    public List<CalculationResponseDto> getCalculationHistory(Long userId) {
        User user = userRepository.findById(userId)
                .filter(u -> u.getUserRole() == UserRole.MANAGER)
                .orElseThrow(() -> new NotFoundException("매니저를 찾을 수 없습니다."));

        List<Calculation> calculations = calculationRepository.findAllByManager_UserId(userId);
        List<CalculationResponseDto> result = new ArrayList<>();

        for (Calculation calc : calculations) {String ids = calc.getReservationIds();
            if (ids == null || ids.isBlank()) continue;

            // 3. 문자열 → Long 리스트 파싱
            List<Long> reservationIds = Arrays.stream(ids.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Long::parseLong)
                    .toList();

            // 4. 예약 리스트 조회
            List<Reservation> reservations = reservationRepository.findAllById(reservationIds);

            // 5. Mapper로 DTO 변환
            for (Reservation reservation : reservations) {result.add(calculationMapper.toDto(reservation, calc));}
        }
        return result;
    }

    /**
     * 특정 기간동안 완료된 예약을 기반으로 정산
     * DONE + 미정산 + 특정 기간
     * @param userId 인증된 매니저의 ID
     * @param weekStart 정산 시작일 (포함)
     * @param weekEnd 정산 종료일 (포함)
     * @return CalculationListWithTotalDto
     */
    @Transactional
    public CalculationListWithTotalDto requestCalculation(Long userId, LocalDate weekStart, LocalDate weekEnd) {

        // 유저 확인 + 역할 검사
        User user = userRepository.findById(userId)
                .filter(u -> u.getUserRole() == UserRole.MANAGER)
                .orElseThrow(() -> new UnauthorizedAccessException("매니저를 찾을 수 없습니다."));

        // 이번 주 정산 금지 (이번 주 월요일 포함 이후면 요청 차단)
        LocalDate thisWeekStart = LocalDate.now().with(DayOfWeek.MONDAY);
        if (!weekEnd.isBefore(thisWeekStart)) {throw new IllegalArgumentException("이번 주는 정산할 수 없습니다. 정산은 지난 주까지만 가능합니다.");}

        // 기간 내 완료된 예약 조회
        List<Reservation> allDoneReservations = reservationRepository
                .findByReservationStatusAndManager_UserIdAndReservationDateBetween(
                        ReservationStatus.DONE, userId, weekStart, weekEnd);

        //  이미 정산된 예약 ID
        List<Calculation> existing = calculationRepository.findAllByManager_UserId(userId);
        Set<Long> alreadyCalculatedIds = existing.stream()
                .flatMap(c -> Arrays.stream(c.getReservationIds().split(",")))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::parseLong)
                .collect(Collectors.toSet());

        List<Reservation> targetReservations = allDoneReservations.stream()
                .filter(res -> !alreadyCalculatedIds.contains(res.getReservationId()))
                .toList();
        if (targetReservations.isEmpty()) {throw new NotFoundException("정산 가능한 예약이 없습니다.");}

        // 총 금액
        int totalAmount = targetReservations.stream()
                .mapToInt(Reservation::getReservationAmount)
                .sum();

        // 예약 ID 문자열로 저장
        String reservationIds = targetReservations.stream()
                .map(res -> res.getReservationId().toString())
                .collect(Collectors.joining(","));

        // 정산 생성
        Calculation calculation = calculationRepository.save(
                Calculation.builder()
                        .manager(user)
                        .startDate(weekStart)
                        .endDate(weekEnd)
                        .amount(totalAmount)
                        .reservationIds(reservationIds)
                        .status(CalculationStatus.PAID)
                        .build()
        );
        List<CalculationResponseDto> list = targetReservations.stream()
                .map(res -> calculationMapper.toDto(res, calculation))
                .toList();

        return new CalculationListWithTotalDto(list, totalAmount);
    }


}
