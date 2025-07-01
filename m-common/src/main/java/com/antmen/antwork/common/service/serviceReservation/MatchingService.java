package com.antmen.antwork.common.service.serviceReservation;

import com.antmen.antwork.common.api.request.reservation.MatchingManagerRequestDto;
import com.antmen.antwork.common.api.request.reservation.MatchingRequestDto;
import com.antmen.antwork.common.api.request.reservation.MatchingResponseRequestDto;
import com.antmen.antwork.common.api.request.alert.AlertRequestDto;
import com.antmen.antwork.common.api.request.reservation.MatchingCancelRequestDto;
import com.antmen.antwork.common.api.response.reservation.MatchingManagerListResponseDto;
import com.antmen.antwork.common.api.response.reservation.ReservationResponseDto;
import com.antmen.antwork.common.domain.entity.account.CustomerAddress;
import com.antmen.antwork.common.domain.entity.account.ManagerDetail;
import com.antmen.antwork.common.domain.entity.account.User;
import com.antmen.antwork.common.domain.entity.account.UserRole;
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

import static jdk.internal.org.jline.utils.Log.warn;

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

        List<Matching> matchingList = new ArrayList<>();
        int basePriority = 1;

        // 자동추천
        if (managerIds == null || managerIds.isEmpty()) {
            managerIds = selectTop3Candidate(reservation);
        }

        for (Long managerId : managerIds) {
            Matching matching = Matching.builder()
                    .reservation(reservation)
                    .manager(userRepository.findById(managerId)
                            .orElseThrow(() -> new IllegalArgumentException("매니저가 없습니다.")))
                    .matchingPriority(basePriority++)
                    .matchingIsRequest(false)
                    .matchingUpdatedAt(LocalDateTime.now())
                    .build();
            matchingList.add(matching);
        }

        matchingRepository.saveAll(matchingList);

        // 1순위에게 알림 전송
        if (!matchingList.isEmpty()) {
            Matching top = matchingList.get(0); // 우선순위대로 추가했으므로 첫 번째가 최우선
            top.setMatchingIsRequest(true);
            top.setMatchingUpdatedAt(LocalDateTime.now());

            alertService.sendAlert(AlertRequestDto.builder()
                    .userId(top.getManager().getUserId())
                    // 예약 상세내용도 보내줘야하나?
                    .alertContent("매칭 요청이 왔어요.")
                    .alertTrigger("Matching")
                    .build());
        }
    }

    // 다음 매칭 요청
    @Transactional
    public void triggerNextMatching(Matching rejectedMatching) {
        Long reservationId = rejectedMatching.getReservation().getReservationId();
        int currentPriority = rejectedMatching.getMatchingPriority();

        Matching nextMatching = matchingRepository
                .findTopByReservation_ReservationIdAndMatchingPriorityGreaterThanOrderByMatchingPriorityAsc(
                        reservationId, currentPriority)
                .orElse(null);

        // 다음 매칭이 없으면 새로운 매칭 생성
        if (nextMatching == null) {
            log.info("🔁 새로운 매칭 생성: reservationId={}, currentPriority={},", reservationId, currentPriority + 1);
            List<Long> newManagers = selectTop3Candidate(rejectedMatching.getReservation());
            List<Matching> newMatchings = new ArrayList<>();
            int basePriority = currentPriority + 1;

            for (Long managerId : newManagers) {
                Matching matching = Matching.builder()
                        .reservation(rejectedMatching.getReservation())
                        .manager(userRepository.findById(managerId)
                                .orElseThrow(() -> new IllegalArgumentException("매니저가 없습니다.")))
                        .matchingPriority(basePriority++)
                        .matchingIsRequest(false)
                        .matchingUpdatedAt(LocalDateTime.now())
                        .build();
                newMatchings.add(matching);
            }
            matchingRepository.saveAll(newMatchings);

            // 매칭할 매니저가 없다면 어떻게 처리할 것인지 고민 필요
            nextMatching = matchingRepository
                    .findTopByReservation_ReservationIdAndMatchingPriorityGreaterThanOrderByMatchingPriorityAsc(
                            reservationId, currentPriority)
                    .orElse(null);
        }

        if (nextMatching != null && nextMatching.getMatchingIsRequest() == true) {
            // 찍히지 않기를 바라지만 찍힌다면 로직 재점검 필요
            log.warn("🚫 매칭이 이미 요청된 매칭입니다. reservationId={}, currentPriority={}", reservationId, currentPriority + 1);
            triggerNextMatching(nextMatching);
            return;
        }

        alertService.sendAlert(AlertRequestDto.builder()
                .userId(nextMatching.getManager().getUserId())
                // 예약 상세내용도 보내줘야하나?
                .alertContent("매칭 요청이 왔어요.")
                .alertTrigger("Matching")
                .build());

        nextMatching.setMatchingIsRequest(true);
        nextMatching.setMatchingUpdatedAt(LocalDateTime.now());
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
        List<User> filteredManager = getFilteredManagers(
                requestDto.getReservationDate(),
                requestDto.getReservationTime(),
                requestDto.getReservationDuration(),
                requestDto.getAddressId(),
                useDistanceFilter
        );
        return sortManagerDtos(filteredManager.stream()
                .map(MatchingManagerListResponseDto::toDto).toList(),sortType);
    }

    // 자동추천 3명
    @Transactional
    public List<MatchingManagerListResponseDto> selectTop3Candidate(MatchingRequestDto requestDto, String sortType) {
        List<User> filteredManager = getFilteredManagers(
                requestDto.getReservationDate(),
                requestDto.getReservationTime(),
                requestDto.getReservationDuration(),
                requestDto.getAddressId(),
                true
        );
        List<MatchingManagerListResponseDto> sortedDtos = sortManagerDtos(filteredManager.stream()
                .map(MatchingManagerListResponseDto::toDto).toList(),sortType);
        return sortedDtos.stream().limit(3).toList();
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS_KM = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
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
            case "distance" -> dtos;
            default -> dtos;
        };
    }

    private List<User> getFilteredManagers(LocalDate date, LocalTime time, int duration, Long addressId, boolean useDistanceFilter) {
        int startTime = time.getHour() * 60 + time.getMinute();
        int endTime = startTime + duration * 60;

        List<Long> busyManagerIds = reservationRepository.findBusyManagerIds(date, startTime, endTime);
        List<User> availableManagers = busyManagerIds.isEmpty()
                ? userRepository.findByUserRole(UserRole.MANAGER)
                : userRepository.findByUserRoleAndUserIdNotIn(UserRole.MANAGER, busyManagerIds);

        if (!useDistanceFilter) {
            return availableManagers;
        }
        CustomerAddress customerAddress = customerAddressRepository.findById(addressId)
                .orElseThrow(() -> new NotFoundException("고객 주소가 존재하지 않습니다."));
        if (customerAddress.getCustomerLatitude() == null || customerAddress.getCustomerLongitude() == null) {
            throw new NotFoundException("고객 주소에 위경도가 존재하지 않습니다.");
        }

        double customerLat = customerAddress.getCustomerLatitude();
        double customerLng = customerAddress.getCustomerLongitude();
        double rangeKm = 10.0;

        List<Long> userIds = availableManagers.stream().map(User::getUserId).toList();
        List<ManagerDetail> managerDetails = managerDetailRepository.findByUserIdIn(userIds);
        Map<Long, User> userMap = availableManagers.stream()
                .collect(Collectors.toMap(User::getUserId, Function.identity()));

        return managerDetails.stream()
                .filter(detail -> {
                    if (detail.getManagerLatitude() == null || detail.getManagerLongitude() == null) {
                        log.warn("위경도 정보가 누락된 매니저: userId={}", detail.getUserId());
                        return false;
                    }
                    return true;
                })
                .filter(detail -> calculateDistance(customerLat, customerLng,
                        detail.getManagerLatitude(),
                        detail.getManagerLongitude()) <= rangeKm)
                .map(detail -> userMap.get(detail.getUserId()))
                .filter(Objects::nonNull)
                .toList();
    }
}
