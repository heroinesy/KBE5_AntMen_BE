package com.antmen.antwork.common.service;

import com.antmen.antwork.common.api.request.ReservationRequestDto;
import com.antmen.antwork.common.api.response.ReservationResponseDto;
import com.antmen.antwork.common.domain.entity.*;
import com.antmen.antwork.common.domain.exception.NotFoundException;
import com.antmen.antwork.common.infra.repository.*;
import com.antmen.antwork.common.service.mapper.ReservationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CategoryOptionRepository categoryOptionRepository;
    private final ReservationOptionRepository reservationOptionRepository;

    private static final short BASE_DURATION = 1;
    private static final int HOURLY_AMOUNT = 20000;

    /**
     * 예약 생성
     */
    @Transactional
    public ReservationResponseDto createReservation(ReservationRequestDto requestDto) {

        User customer = userRepository.findById(requestDto.getCustomerId())
                .orElseThrow(() -> new NotFoundException("해당 유저를 찾을 수 없습니다"));

        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(()->new NotFoundException("해당 카테고리가 존재하지 않습니다."));

        // 옵션 리스트
        List<Long> optionIds = Optional.ofNullable(requestDto.getOptionIds())
                .filter(ids->!ids.isEmpty()).orElse(Collections.emptyList());

        List<CategoryOption> selectedOptions = optionIds.isEmpty()
                ? Collections.emptyList()
                : categoryOptionRepository.findAllById(optionIds);

        // 총 예약 시간
        short additionalDuration = requestDto.getAdditionalDuration();
        short totalDuration = (short)(BASE_DURATION + additionalDuration);

        // 총 가격 계산
        int totalAmount = totalDuration * HOURLY_AMOUNT
                + selectedOptions.stream()
                .mapToInt(CategoryOption::getCoPrice)
                .sum();

        // 예약 저장
        Reservation reservation = reservationMapper.toEntity(
                requestDto,
                customer,
                category,
                totalDuration,
                totalAmount
        );
        Reservation saved = reservationRepository.save(reservation);

        // 옵션 저장
        List<ReservationOption> reservationOptions = selectedOptions.stream()
                .map(option -> ReservationOption.builder()
                        .reservation(saved)
                        .categoryOption(option)
                        .build())
                .collect(Collectors.toList());

        if (!reservationOptions.isEmpty()) {
            reservationOptionRepository.saveAll(reservationOptions);}
        return reservationMapper.toDto(saved, reservationOptions);
    }

    /**
     * 예약 조회
     */
    @Transactional
    public ReservationResponseDto getReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 예약이 존재하지 않습니다."));

        List<ReservationOption> options = reservationOptionRepository
                .findByReservation_ReservationId(reservation.getReservationId());

        return reservationMapper.toDto(reservation, options);

    }

    /**
     * 예약 상태 변경
     */
    @Transactional
    public void changeStatus(Long id, String status) {
        if (!ReservationStatus.isValidCode(status)) {
            throw new IllegalArgumentException("유효하지 않은 예약 상태입니다.");
        }

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));

        reservation.setReservationStatus(ReservationStatus.fromCode(status));
        reservationRepository.save(reservation);
    }

    /**
     * 예약 취소
     */
    @Transactional
    public void cancelReservation(Long id, String cancelReason) {

        if (cancelReason == null || cancelReason.trim().isEmpty()) {
            throw new IllegalArgumentException("취소 사유는 필수입니다.");
        }

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));

        reservation.setReservationStatus(ReservationStatus.CANCEL);
        reservation.setReservationCancelReason(cancelReason);
        reservationRepository.save(reservation);
    }
}
