package com.antmen.antwork.common.service.serviceReservation;

import com.antmen.antwork.common.api.request.reservation.MatchingManagerRequestDto;
import com.antmen.antwork.common.api.request.reservation.MatchingRequestDto;
import com.antmen.antwork.common.api.request.reservation.MatchingResponseRequestDto;
import com.antmen.antwork.common.api.request.alert.AlertRequestDto;
import com.antmen.antwork.common.api.request.reservation.MatchingCancelRequestDto;
import com.antmen.antwork.common.api.response.reservation.MatchingManagerListResponseDto;
import com.antmen.antwork.common.api.response.reservation.ReservationResponseDto;
import com.antmen.antwork.common.domain.entity.account.ManagerDetail;
import com.antmen.antwork.common.domain.entity.account.User;
import com.antmen.antwork.common.domain.entity.account.UserRole;
import com.antmen.antwork.common.domain.entity.reservation.Matching;
import com.antmen.antwork.common.domain.entity.reservation.Reservation;
import com.antmen.antwork.common.domain.entity.reservation.ReservationStatus;
import com.antmen.antwork.common.infra.repository.account.ManagerDetailRepository;
import com.antmen.antwork.common.infra.repository.reservation.ReservationRepository;
import com.antmen.antwork.common.infra.repository.account.UserRepository;
import com.antmen.antwork.common.infra.repository.reservation.MatchingRepository;
import com.antmen.antwork.common.service.AlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
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

            try {
                alertService.sendAlert(AlertRequestDto.builder()
                        .userId(top.getManager().getUserId())
                        // 예약 상세내용도 보내줘야하나?
                        .alertContent("매칭 요청이 왔어요.")
                        .alertTrigger("Matching")
                        .redirectUrl("/manager/matching")
                        .build());
            } catch (Exception e) {
                // 알림 발송에 실패하더라도 트랜잭션 롤백 x
                log.error("매칭 ID {}에 대한 알림 발송에 실패했습니다. 하지만 매칭은 정상적으로 생성됩니다.", top.getManager().getUserId(), e);
            }
        }
    }

    // 자동추천 3명
    // 이건 재매칭 때 사용하는 걸로
    @Transactional
    public List<Long> selectTop3Candidate(Reservation reservation) {
        // TODO: 추후에 조건추가 예정
        return userRepository.findTop3AvailableManagers(reservation.getReservationId(), PageRequest.of(0, 3));
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
                .redirectUrl("/manager/matching")
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
                    .redirectUrl("/myreservation/"+matching.getReservation().getReservationId())
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
        int startTime = requestDto.getReservationTime().getHour() * 60 + requestDto.getReservationTime().getMinute();
        int endTime = startTime + requestDto.getReservationDuration() * 60;


         // 시간순 (기본) : 수요자 예약 시간 기준 겹치는 매니저 제외
        List<Long> busyManagerIds = reservationRepository.findBusyManagerIds(
                requestDto.getReservationDate(), startTime, endTime);
        List<User> availableManagers = userRepository.findByUserRoleAndUserIdNotIn(
                UserRole.MANAGER, busyManagerIds);

        // 분기 처리 (시간, 시간+거리)
       if (!useDistanceFilter) {
           return sortManagerDtos(availableManagers.stream()
                   .map(MatchingManagerListResponseDto::toDto)
                   .toList(), sortType);
       }

        // 거리순 : 수요자 기준 10km 이내 필터링
        double customerLat = 37.5665;  // 예: 서울시청
        double customerLng = 126.9780;
        double rangeKm = 10.0;

        List<Long> userIds = availableManagers.stream().map(User::getUserId).toList();
        List<ManagerDetail> managerDetails = managerDetailRepository.findByUserIdIn(userIds);
        List<MatchingManagerListResponseDto> filtered = managerDetails.stream()
                .filter(detail -> detail.getManagerLatitude() != null && detail.getManagerLongitude() != null)
                .filter(detail -> calculateDistance(customerLat, customerLng, detail.getManagerLatitude(), detail.getManagerLongitude()) <= rangeKm)
                .map(detail -> {
                    User user = availableManagers.stream()
                            .filter(u -> u.getUserId().equals(detail.getUserId()))
                            .findFirst()
                            .orElse(null);
                    return user != null ? MatchingManagerListResponseDto.toDto(user) : null;
                })
                .filter(Objects::nonNull)
                .toList();

        return sortManagerDtos(filtered, sortType);
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
}
