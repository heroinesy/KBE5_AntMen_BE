package com.antmen.antwork.common.service.serviceReservation;

import com.antmen.antwork.common.api.request.reservation.MatchingManagerRequestDto;
import com.antmen.antwork.common.api.request.reservation.MatchingRequestDto;
import com.antmen.antwork.common.api.request.reservation.MatchingResponseRequestDto;
import com.antmen.antwork.common.api.request.alert.AlertRequestDto;
import com.antmen.antwork.common.api.request.reservation.MatchingCancelRequestDto;
import com.antmen.antwork.common.api.response.reservation.MatchingManagerListResponseDto;
import com.antmen.antwork.common.api.response.reservation.ReservationResponseDto;
import com.antmen.antwork.common.domain.entity.account.*;
import com.antmen.antwork.common.domain.entity.reservation.Matching;
import com.antmen.antwork.common.domain.entity.reservation.Reservation;
import com.antmen.antwork.common.domain.entity.reservation.ReservationStatus;
import com.antmen.antwork.common.domain.exception.NotFoundException;
import com.antmen.antwork.common.infra.repository.account.CustomerAddressRepository;
import com.antmen.antwork.common.infra.repository.account.ManagerDetailRepository;
import com.antmen.antwork.common.infra.repository.reservation.ReservationRepository;
import com.antmen.antwork.common.infra.repository.account.UserRepository;
import com.antmen.antwork.common.infra.repository.reservation.MatchingRepository;
import com.antmen.antwork.common.service.AlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Manager;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchingService {
    private final MatchingRepository matchingRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final AlertService alertService;
    private final ManagerDetailRepository managerDetailRepository;
    private final CustomerAddressRepository customerAddressRepository;

    // 매칭 생성
    @Transactional
    public void initiateMatching(Long reservationId, List<Long> managerIds) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약이 존재하지 않습니다."));

        MatchingRequestDto matchingRequestDto = MatchingRequestDto.builder()
                .reservationId(reservation.getReservationId())
                .addressId(reservation.getAddress().getAddressId())
                .reservationDate(reservation.getReservationDate())
                .reservationTime(reservation.getReservationTime())
                .reservationDuration(reservation.getReservationDuration())
                .build();

        // 자동추천
        List<Long> selectedManagerIds = (managerIds == null || managerIds.isEmpty())
                ? selectTop3Candidate(matchingRequestDto, "distance").stream()
                .map(MatchingManagerListResponseDto::getManagerId)
                .toList()
                : managerIds;

        List<Matching> matchingList = new ArrayList<>();
        int basePriority = 1;


        for (Long managerId : selectedManagerIds) {
            User manager = userRepository.findById(managerId)
                    .orElseThrow(() -> new IllegalArgumentException("매니저가 없습니다."));
            Matching matching = Matching.builder()
                    .reservation(reservation)
                    .manager(manager)
                    .matchingPriority(basePriority++)
                    .matchingIsRequest(false)
                    .matchingUpdatedAt(LocalDateTime.now())
                    .build();
            matchingList.add(matching);
        }
        matchingRepository.saveAll(matchingList);

        // 1순위에게 알림 전송
        if (!matchingList.isEmpty()) {
            Matching top = matchingList.get(0); // 우선 순위대로 추가했으므로 첫 번째가 최우선
            top.setMatchingIsRequest(true);
            top.setMatchingUpdatedAt(LocalDateTime.now());

            alertService.sendAlert(AlertRequestDto.builder()
                    .userId(top.getManager().getUserId())
                    .alertContent("매칭 요청이 왔어요.")
                    .alertTrigger("Matching")
                    .build());
        }
    }

    // 다음 매칭 요청
    @Transactional
    public void triggerNextMatching(Matching rejectedMatching) {
        Long reservationId = rejectedMatching.getReservation().getReservationId();
        Reservation reservation = rejectedMatching.getReservation();
        int currentPriority = rejectedMatching.getMatchingPriority();

        Matching nextMatching = null;
        int tryPriority = currentPriority + 1;

        while (true) {
            nextMatching = matchingRepository
                    .findTopByReservation_ReservationIdAndMatchingPriorityGreaterThanOrderByMatchingPriorityAsc(
                            reservationId, tryPriority - 1)
                    .orElse(null);

            // 다음 매칭이 없으면 새 매칭 생성 시도
            if (nextMatching == null) {
                log.info("🔁 새로운 매칭 생성: reservationId={}, currentPriority={}", reservationId, tryPriority);

                MatchingRequestDto matchingRequestDto = MatchingRequestDto.builder()
                        .reservationId(reservation.getReservationId())
                        .addressId(reservation.getAddress().getAddressId())
                        .reservationDate(reservation.getReservationDate())
                        .reservationTime(reservation.getReservationTime())
                        .reservationDuration(reservation.getReservationDuration())
                        .build();

                List<Long> newManagerIds = selectTop3Candidate(matchingRequestDto, "distance").stream()
                        .map(MatchingManagerListResponseDto::getManagerId).toList();

                int newPriority = tryPriority;
                List<Matching> newMatchings = new ArrayList<>();
                for (Long managerId : newManagerIds) {
                    User manager = userRepository.findById(managerId)
                            .orElseThrow(() -> new IllegalArgumentException("매니저가 없습니다."));
                    Matching newMatching = Matching.builder()
                            .reservation(reservation)
                            .manager(manager)
                            .matchingPriority(newPriority++)
                            .matchingIsRequest(false)
                            .matchingUpdatedAt(LocalDateTime.now())
                            .build();
                    newMatchings.add(newMatching);
                }
                matchingRepository.saveAll(newMatchings);

                nextMatching = newMatchings.isEmpty() ? null : newMatchings.get(0);
            }

            if (nextMatching == null) {
                log.info("매칭할 수 있는 매니저가 더 이상 없습니다.");
                return;}

            if (!Boolean.TRUE.equals(nextMatching.getMatchingIsRequest())) {
                nextMatching.setMatchingIsRequest(true);
                nextMatching.setMatchingUpdatedAt(LocalDateTime.now());

                alertService.sendAlert(AlertRequestDto.builder()
                        .userId(nextMatching.getManager().getUserId())
                        .alertContent("매칭 요청이 왔어요.")
                        .alertTrigger("Matching")
                        .build());
                return;
            }

            // 이미 요청된 경우 다음 순위로
            tryPriority++;
        }
    }

    // 매니저 매칭 답장
    @Transactional
    public void managerRespondMatching(Long matchingId, MatchingManagerRequestDto matchingManagerRequestDto) {
        Matching matching = matchingRepository.findById(matchingId)
                .orElseThrow(() -> new IllegalArgumentException("매칭 정보를 찾을 수 없습니다."));

        if (matching.getMatchingManagerIsAccept() != null) {
            throw new IllegalStateException("이미 응답한 매칭입니다.");
        }

        matching.setMatchingManagerIsAccept(matchingManagerRequestDto.getMatchingManagerIsAccept());

        if (matchingManagerRequestDto.getMatchingRefuseReason() != null) {
            matching.setMatchingRefuseReason(matchingManagerRequestDto.getMatchingRefuseReason());
        }

        matching.setMatchingUpdatedAt(LocalDateTime.now());

        // 수락시 수요자에게 알림
        if (matching.getMatchingManagerIsAccept()) {
            alertService.sendAlert(AlertRequestDto.builder()
                    .userId(matching.getReservation().getCustomer().getUserId())
                    .alertContent("매칭이 수락되었습니다.")
                    .alertTrigger("Matching")
                    .build());
        } else {
            // 거절시 다음 순위로 넘어감
            triggerNextMatching(matching);
        }
    }

    // 수요자 매칭 답장
    @Transactional
    public void customerResponseMatching(Long matchingId, MatchingResponseRequestDto requestDto) {
        Matching matching = matchingRepository.findById(matchingId)
                .orElseThrow(() -> new IllegalArgumentException("매칭 정보를 찾을 수 없습니다."));

        if (matching.getMatchingIsFinal() != null) {
            throw new IllegalStateException("이미 응답한 매칭입니다.");
        }
        matching.setMatchingIsFinal(requestDto.getMatchingIsFinal());

        if (requestDto.getMatchingRefuseReason() != null) {
            matching.setMatchingRefuseReason(requestDto.getMatchingRefuseReason());
        }

        Reservation reservation = matching.getReservation();

        // 매칭 수락 시
        if (matching.getMatchingIsFinal()) {
            reservation.setReservationStatus(ReservationStatus.MATCHING);
            reservation.setManager(matching.getManager());
            reservation.setMatchedAt(LocalDateTime.now());

            // 다른 매니저들에게 이미 매칭되었다고 알림
            List<Matching> otherMatchings = matchingRepository
                    .findAllByReservation_ReservationId(reservation.getReservationId());
            for (Matching m : otherMatchings) {
                if (m.getMatchingId() != matchingId) {
                    // m.setMatchingIsFinal(false);
                    // m.setMatchingRefuseReason("타 매칭 수락");
                    // m.setMatchingUpdatedAt(LocalDateTime.now());

                    if (m.getMatchingIsRequest()) {
                        alertService.sendAlert(AlertRequestDto.builder()
                                .userId(m.getManager().getUserId())
                                .alertContent("다른 매니저와 매칭이 완료되었습니다.")
                                .alertTrigger("Matching")
                                .build());
                    }
                }
            }
        }

        matching.setMatchingUpdatedAt(LocalDateTime.now());
    }

    // 매칭거절
    @Transactional
    public void cancelMatching(Long matchingId, MatchingCancelRequestDto requestDto) {
        if (requestDto.getIsContinue()) {
            triggerNextMatching(matchingRepository.findById(matchingId).get());
        } else {
            Reservation reservation = matchingRepository.findById(matchingId).get().getReservation();
            reservation.setReservationStatus(ReservationStatus.CANCEL);
            reservation.setReservationCancelReason(requestDto.getCancelReason());

            // 취소된 예약에 대해 매니저들에게 예약 취소 알람
            List<Matching> requestedMatching = matchingRepository
                    .findAllByReservation_ReservationId(reservation.getReservationId());

            for (Matching m : requestedMatching) {
                if (m.getMatchingId() != matchingId) {
                    // m.setMatchingIsFinal(false);
                    // m.setMatchingRefuseReason("취소된 예약");
                    // m.setMatchingUpdatedAt(LocalDateTime.now());
                    if (m.getMatchingIsRequest()) {
                        if (m.getMatchingManagerIsAccept() || m.getMatchingManagerIsAccept() == null) {
                            alertService.sendAlert(AlertRequestDto.builder()
                                    .userId(m.getManager().getUserId())
                                    .alertContent("고객이 취소한 예약입니다.")
                                    .alertTrigger("Matching")
                                    .build());
                        }
                    }
                }
            }
        }
    }

    // 매칭 신청 가능한 매니저 리스트 조회 (시간)
    @Transactional(readOnly = true)
    public List<MatchingManagerListResponseDto> getManagerList(MatchingRequestDto requestDto, boolean useDistanceFilter, String sortType) {
        List<MatchingManagerListResponseDto> filteredManager = getFilteredManagers(
                requestDto.getReservationDate(),
                requestDto.getReservationTime(),
                requestDto.getReservationDuration(),
                requestDto.getAddressId(),
                useDistanceFilter,
                null,
                false
        );
        return sortManagerDtos(filteredManager,sortType);
    }

    // 자동추천 3명
    @Transactional
    public List<MatchingManagerListResponseDto> selectTop3Candidate(MatchingRequestDto requestDto, String sortType) {
        List<MatchingManagerListResponseDto> filteredManager = getFilteredManagers(
                requestDto.getReservationDate(),
                requestDto.getReservationTime(),
                requestDto.getReservationDuration(),
                requestDto.getAddressId(),
                true,
                requestDto.getReservationId(),
                true
        );
        return sortManagerDtos(filteredManager,sortType).stream().limit(3).toList();
    }

    /**
     * 매칭 유틸 메소드
     * 1. 위경도 계산 calculateDistance
     * 2. 정렬 (리뷰, 최근 가입순, 거리순) sortManagerDtos
     * 3. 매칭 추천 (시간, 거리 적용) getFilteredManagers
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS_KM = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double rLat1 = Math.toRadians(lat1);
        double rLat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(rLat1) * Math.cos(rLat2)
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

    private List<MatchingManagerListResponseDto> sortManagerDtos(List<MatchingManagerListResponseDto> dtos, String sortType) {
        return switch (sortType.toLowerCase()) {
            // todo: 리뷰 기반 정렬은 reviewSummary 기능 구현 후 활성화 진행할게용
//            case "review" -> dtos.stream()
//                    .sorted(Comparator.comparingDouble(MatchingManagerListResponseDto::getManagerRating).reversed())
//                    .toList();
            case "recent" -> dtos.stream()
                    .sorted(Comparator.comparing(MatchingManagerListResponseDto::getManagerId).reversed())
                    .toList();
            case "distance" -> dtos.stream()
                    .sorted(Comparator.comparingDouble(dto -> Optional.ofNullable(dto.getDistance()).orElse(Double.MAX_VALUE)))
                    .toList();
            default -> dtos;
        };
    }

    private List<User> getAvailableManagers(LocalDate date, LocalTime time, int duration) {
        int startTime = time.getHour() * 60 + time.getMinute();
        int endTime = startTime + duration * 60;
        List<Long> busyManagerIds = reservationRepository.findBusyManagerIds(date, startTime, endTime);

        List<User> baseManagers = busyManagerIds.isEmpty()
                ? userRepository.findByUserRole(UserRole.MANAGER)
                : userRepository.findByUserRoleAndUserIdNotIn(UserRole.MANAGER, busyManagerIds);

        List<Long> approvedManagerIds = managerDetailRepository.findByManagerStatus(ManagerStatus.APPROVED).stream()
                .map(ManagerDetail::getUserId).toList();
        return baseManagers.stream()
                .filter(user -> approvedManagerIds.contains(user.getUserId()))
                .toList();
    }

    private List<User> excludeAlreadyMatchedManagers(List<User> candidates, Long reservationId) {
        if (reservationId == null) return candidates;

        List<Long> alreadyMatched = matchingRepository.findAllByReservation_ReservationId(reservationId).stream()
                .map(m -> m.getManager().getUserId())
                .toList();

        return candidates.stream()
                .filter(user -> !alreadyMatched.contains(user.getUserId()))
                .toList();
    }

    private List<MatchingManagerListResponseDto> filterManagersByDistance(List<User> managers, Long addressId) {
        CustomerAddress address = customerAddressRepository.findById(addressId)
                .orElseThrow(() -> new NotFoundException("고객 주소가 존재하지 않습니다."));
        if (address.getCustomerLatitude() == null || address.getCustomerLongitude() == null) {
            throw new NotFoundException("고객 주소에 위경도가 존재하지 않습니다.");}

        double lat = address.getCustomerLatitude();
        double lng = address.getCustomerLongitude();
        double rangeKm = 10.0;

        Map<Long, User> userMap = managers.stream().collect(Collectors.toMap(User::getUserId, Function.identity()));
        List<ManagerDetail> managerDetails = managerDetailRepository.findByUserIdIn(userMap.keySet().stream().toList());

        return managerDetails.stream()
                .filter(d -> d.getManagerLatitude() != null && d.getManagerLongitude() != null)
                .map(d -> {
                    double distance = calculateDistance(lat, lng, d.getManagerLatitude(), d.getManagerLongitude());
                    if (distance > rangeKm) return null;

                    User user = userMap.get(d.getUserId());
                    return user != null ? MatchingManagerListResponseDto.toDto(user, distance) : null;
                })
                .filter(Objects::nonNull)
                .toList();
    }

    private List<MatchingManagerListResponseDto> getFilteredManagers(LocalDate date, LocalTime time, int duration, Long addressId, boolean useDistanceFilter, Long reservationId, boolean excludeAlreadyMatched) {
        List<User> availableManagers = getAvailableManagers(date, time, duration);

        if (excludeAlreadyMatched && reservationId != null) {
            availableManagers = excludeAlreadyMatchedManagers(availableManagers, reservationId);}
        if (useDistanceFilter) {
            return filterManagersByDistance(availableManagers, addressId);}

        return availableManagers.stream().map(MatchingManagerListResponseDto::toDto).toList();
    }
}