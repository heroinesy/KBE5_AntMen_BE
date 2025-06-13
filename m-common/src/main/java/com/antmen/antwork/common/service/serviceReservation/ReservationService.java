package com.antmen.antwork.common.service.serviceReservation;

import com.antmen.antwork.common.api.request.reservation.ReservationRequestDto;
import com.antmen.antwork.common.api.response.reservation.*;
import com.antmen.antwork.common.domain.entity.ReviewSummary;
import com.antmen.antwork.common.domain.entity.account.CustomerAddress;
import com.antmen.antwork.common.domain.entity.account.ManagerDetail;
import com.antmen.antwork.common.domain.entity.account.User;
import com.antmen.antwork.common.domain.entity.account.UserRole;
import com.antmen.antwork.common.domain.entity.reservation.*;
import com.antmen.antwork.common.domain.exception.NotFoundException;
import com.antmen.antwork.common.domain.exception.UnauthorizedAccessException;
import com.antmen.antwork.common.infra.repository.account.CustomerAddressRepository;
import com.antmen.antwork.common.infra.repository.account.ManagerDetailRepository;
import com.antmen.antwork.common.infra.repository.account.UserRepository;
import com.antmen.antwork.common.infra.repository.reservation.*;
import com.antmen.antwork.common.service.mapper.reservation.ReservationMapper;
import com.antmen.antwork.common.service.rule.ServiceTimeAdvisor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CategoryOptionRepository categoryOptionRepository;
    private final ReservationOptionRepository reservationOptionRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final MatchingService matchingService;
    private final ReservationDtoConverter reservationDtoConverter;
    private final ManagerDetailRepository managerDetailRepository;
    private final ReviewSummaryRepository reviewSummaryRepository;
    private final MatchingRepository matchingRepository;

    /**
     * 예약 단위
     */
    private static final short BASE_DURATION = 1; // 기본 시간
    private static final int HOURLY_AMOUNT = 20000; // 시간당 가격
    private final ServiceTimeAdvisor serviceTimeAdvisor; // 면적 기반 추천 시간

    /**
     * 예약 생성
     */
    @Transactional
    public ReservationResponseDto createReservation(ReservationRequestDto requestDto) {

        User customer = userRepository.findById(requestDto.getCustomerId())
                .orElseThrow(() -> new NotFoundException("해당 유저를 찾을 수 없습니다"));

        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(()->new NotFoundException("해당 카테고리가 존재하지 않습니다."));

        CustomerAddress address = customerAddressRepository.findById(requestDto.getAddressId())
                .orElseThrow(()->new NotFoundException("등록된 주소가 없습니다."));
        if (!address.getUser().getUserId().equals(customer.getUserId())) {
            throw new UnauthorizedAccessException("해당 주소에 접근할 수 없습니다.");
        }

        // 옵션 리스트
        List<Long> optionIds = Optional.ofNullable(requestDto.getOptionIds())
                .filter(ids->!ids.isEmpty()).orElse(Collections.emptyList());

        List<CategoryOption> selectedOptions = optionIds.isEmpty()
                ? Collections.emptyList()
                : categoryOptionRepository.findAllById(optionIds);

        // 면적 기반 추천 시간
        int area = address.getAddressArea();
        short recommendDuration = serviceTimeAdvisor.recommedTime(area);

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

        // 매니저 저장
        matchingService.initiateMatching(saved.getReservationId(), requestDto.getManagerIds());
        return reservationMapper.toDto(saved, reservationOptions, recommendDuration);
    }

    /**
     * 예약 단건 조회
     * 예약 폼 확인 페이지에서 사용
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
     * 수요자 예약 기본 정보 목록 조회 (customer)
     * @return category, time, status
     * 카드형 간략 조회
     */
    @Transactional(readOnly = true)
    public List<ReservationResponseDto> getReservationsByCustomer(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("유저가 존재하지 않습니다."));

        if (user.getUserRole() != UserRole.CUSTOMER) {
            throw new UnauthorizedAccessException("고객만 접근할 수 있는 API입니다.");
        }

        List<Reservation> reservations = reservationRepository.findByCustomer_UserId(userId);
        return mapReservationsToDtos(reservations);
    }

    /**
     * 매니저 예약 기본 정보 목록 조회 (manager)
     * @return category, time, state
     */
    @Transactional(readOnly = true)
    public List<ReservationResponseDto> getReservationsByManager(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("유저가 존재하지 않습니다."));

        if (user.getUserRole() != UserRole.MANAGER) {
            throw new UnauthorizedAccessException("매니저만 접근할 수 있는 API입니다.");
        }

        List<Reservation> reservations = reservationRepository.findByManager_UserId(userId);
        return mapReservationsToDtos(reservations);
    }

    /**
     * 예약 + 매칭 + 주소
     * 상세보기 페이지
     * @param reservationId
     * @return
     */
    @Transactional(readOnly = true)
    public ReservationHistoryDto getReservationDetail(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("예약이 존재하지 않습니다."));

        return reservationDtoConverter.toDto(reservation);
    }

    /**
     * 관리자 예약 목록 조회 (admin)
     */
    @Transactional(readOnly = true)
    public List<ReservationResponseDto> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return mapReservationsToDtos(reservations);
    }

    /**
     * 매니저 예약 상태 변경 (manager)
     */
    @Transactional
    public void changeStatusByManager(Long reservationId, Long managerId, String statusCode) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("예약이 존재하지 않습니다."));

        validateManagerAuthority(reservation, managerId);
        validateAndSetStatus(reservation, statusCode);
    }

    /**
     * 관리자 예약 상태 변경 (admin)
     */
    @Transactional
    public void changeStatusByAdmin(Long reservationId, String statusCode) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("예약이 존재하지 않습니다."));
        validateAndSetStatus(reservation, statusCode);
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

    /**
     * 매니저에게 매칭 요청된 예약 목록 조회
     * 매칭 내역 + 기본 예약 정보 포함
     */
    @Transactional(readOnly = true)
    public List<ReservationHistoryDto> getReservationsByMatchingManager(Long managerId) {
        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new NotFoundException("해당 매니저가 존재하지 않습니다."));

        List<Matching> matchings = matchingRepository.findAllByManagerAndMatchingManagerIsAccept(manager);
        List<Reservation> reservations = matchings.stream()
                .map(Matching::getReservation).collect(Collectors.toList());
        return reservationDtoConverter.convertToDtos(reservations);
    }

    /**
     * reservation Util
     */
    public List<ReservationResponseDto> mapReservationsToDtos(List<Reservation> reservations) {
        return reservations.stream()
                .map(reservation -> {
                    List<ReservationOption> options =
                            reservationOptionRepository.findByReservation_ReservationId(reservation.getReservationId());
                    return reservationMapper.toDto(reservation, options);
                })
                .collect(Collectors.toList());
    }

    private void validateManagerAuthority(Reservation reservation, Long managerId) {
        if (reservation.getManager() == null ||
                !reservation.getManager().getUserId().equals(managerId)) {
            throw new UnauthorizedAccessException("해당 예약에 대한 권한이 없습니다.");
        }
    }

    private void validateAndSetStatus(Reservation reservation, String statusCode) {
        if (!ReservationStatus.isValidCode(statusCode)) {
            throw new IllegalArgumentException("유효하지 않은 예약 상태 코드입니다: " + statusCode);
        }
        reservation.setReservationStatus(ReservationStatus.fromCode(statusCode));
    }

    public MatchingManagerDetailResponseDto getManagerDetail(Long id) {
        MatchingManagerDetailResponseDto responseDto = new MatchingManagerDetailResponseDto();

        User manager = userRepository.findById(id).get();
        ManagerDetail detail = managerDetailRepository.findById(id).get();
        ReviewSummary reviewSummary = reviewSummaryRepository.findById(id).get();

        return responseDto.toDto(manager, detail, reviewSummary);

    }
}