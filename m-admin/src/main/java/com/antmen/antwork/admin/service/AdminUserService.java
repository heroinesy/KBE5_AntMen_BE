package com.antmen.antwork.admin.service;

import com.antmen.antwork.admin.api.AdminUserResponseDto;
import com.antmen.antwork.admin.api.CustomerDetailResponseDto;
import com.antmen.antwork.admin.api.ManagerDetailResponseDto;
import com.antmen.antwork.common.api.response.CustomerReservationStatisticsDto;
import com.antmen.antwork.common.api.response.CustomerReviewInfoDto;
import com.antmen.antwork.common.api.response.CustomerReviewDto;
import com.antmen.antwork.common.api.response.ManagerMatchingStatisticsDto;
import com.antmen.antwork.common.api.response.ManagerWorkHistoryDto;
import com.antmen.antwork.common.domain.entity.account.User;
import com.antmen.antwork.common.domain.entity.account.UserGender;
import com.antmen.antwork.common.domain.entity.account.UserRole;
import com.antmen.antwork.common.domain.entity.account.ManagerDetail;
import com.antmen.antwork.common.domain.entity.reservation.Reservation;
import com.antmen.antwork.common.domain.entity.reservation.ReservationStatus;
import com.antmen.antwork.common.domain.entity.reservation.Review;
import com.antmen.antwork.common.domain.entity.reservation.ReviewAuthorType;
import com.antmen.antwork.common.infra.repository.account.UserRepository;
import com.antmen.antwork.common.infra.repository.account.ManagerDetailRepository;
import com.antmen.antwork.common.infra.repository.reservation.RefundRepository;
import com.antmen.antwork.common.infra.repository.reservation.ReservationRepository;
import com.antmen.antwork.common.infra.repository.reservation.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;
import com.antmen.antwork.common.api.response.ManagerReviewInfoDto;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminUserService {
    private final UserRepository userRepository;
    private final ManagerDetailRepository managerDetailRepository;
    private final ReservationRepository reservationRepository;
    private final ReviewRepository reviewRepository;
    private final RefundRepository refundRepository;

    /**
     * 수요자 상세정보 통합 조회 (기본정보 + 예약통계 + 리뷰정보)
     */
    public CustomerDetailResponseDto getCustomerDetail(Long userId) {
        // 수요자 존재 여부 확인
        User user = userRepository.findById(userId)
                .filter(u -> u.getUserRole() == UserRole.CUSTOMER)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 수요자입니다."));

        // 기본 정보
        AdminUserResponseDto userInfo = convertToDto(user);
        
        // 예약 통계
        CustomerReservationStatisticsDto reservationStatistics = getCustomerReservationStatistics(userId);
        
        // 리뷰 정보
        CustomerReviewInfoDto reviewInfo = getCustomerReviewInfo(userId);

        return CustomerDetailResponseDto.builder()
                .userInfo(userInfo)
                .reservationStatistics(reservationStatistics)
                .reviewInfo(reviewInfo)
                .build();
    }

    /**
     * 매니저 상세정보 통합 조회 (기본정보 + 매칭통계 + 근무내역)
     */
    public ManagerDetailResponseDto getManagerDetail(Long userId) {
        // 매니저 존재 여부 확인
        User user = userRepository.findById(userId)
                .filter(u -> u.getUserRole() == UserRole.MANAGER)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매니저입니다."));

        // 기본 정보
        AdminUserResponseDto userInfo = convertToDto(user);
        
        // 매칭 통계
        ManagerMatchingStatisticsDto matchingStatistics = getManagerMatchingStatistics(userId);
        
        // 근무 내역
        List<ManagerWorkHistoryDto> workHistory = getManagerWorkHistory(userId);

        // 리뷰 정보
        ManagerReviewInfoDto reviewInfo = getManagerReviewInfo(userId);

        return ManagerDetailResponseDto.builder()
                .userInfo(userInfo)
                .matchingStatistics(matchingStatistics)
                .workHistory(workHistory)
                .reviewInfo(reviewInfo)
                .build();
    }

    /**
     * 매니저별 매칭 통계 조회
     */
    public ManagerMatchingStatisticsDto getManagerMatchingStatistics(Long userId) {
        // 매니저 존재 여부 확인
        User user = userRepository.findById(userId)
                .filter(u -> u.getUserRole() == UserRole.MANAGER)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매니저입니다."));

        // 매칭 통계 계산
        List<Reservation> managerReservations = reservationRepository.findByManager_UserId(userId);
        
        long totalMatchings = managerReservations.size();
        long completedMatchings = managerReservations.stream()
                .filter(r -> r.getReservationStatus() == ReservationStatus.DONE)
                .count();
        long pendingMatchings = managerReservations.stream()
                .filter(r -> r.getReservationStatus() == ReservationStatus.WAITING || 
                           r.getReservationStatus() == ReservationStatus.MATCHING)
                .count();
        long cancelledMatchings = managerReservations.stream()
                .filter(r -> r.getReservationStatus() == ReservationStatus.CANCEL)
                .count();

        // 성공률 계산
        double successRate = totalMatchings > 0 ? 
                (double) completedMatchings / totalMatchings * 100 : 0.0;

        // 평균 평점 계산 (매니저가 받은 리뷰의 평균)
        List<Review> receivedReviews = reviewRepository.findByReviewAuthorAndReviewManager_UserId(
                ReviewAuthorType.CUSTOMER, userId);
        double averageRating = receivedReviews.stream()
                .mapToInt(Review::getReviewRating)
                .average()
                .orElse(0.0);

        return ManagerMatchingStatisticsDto.builder()
                .totalMatchings(totalMatchings)
                .completedMatchings(completedMatchings)
                .pendingMatchings(pendingMatchings)
                .cancelledMatchings(cancelledMatchings)
                .successRate(Math.round(successRate * 100.0) / 100.0)
                .averageRating(Math.round(averageRating * 100.0) / 100.0)
                .build();
    }

    /**
     * 매니저별 근무 내역 조회
     */
    public List<ManagerWorkHistoryDto> getManagerWorkHistory(Long userId) {
        // 매니저 존재 여부 확인
        User user = userRepository.findById(userId)
                .filter(u -> u.getUserRole() == UserRole.MANAGER)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매니저입니다."));

        // 완료된 예약만 조회 (근무 내역)
        List<Reservation> completedReservations = reservationRepository.findByManager_UserIdAndReservationStatus(userId, ReservationStatus.DONE);
        
        return completedReservations.stream()
                .sorted((r1, r2) -> r2.getReservationDate().compareTo(r1.getReservationDate())) // 최신순 정렬
                .map(reservation -> ManagerWorkHistoryDto.builder()
                        .reservationId(reservation.getReservationId())
                        .customerName(reservation.getCustomer().getUserName())
                        .serviceName(reservation.getCategory().getCategoryName())
                        .workDate(reservation.getReservationDate().toString())
                        .rating(getReservationRating(reservation.getReservationId()))
                        .build())
                .collect(Collectors.toList());
    }

    private Short getReservationRating(Long reservationId) {
        Review review = reviewRepository.findByReservation_ReservationIdAndReviewAuthor(reservationId, ReviewAuthorType.CUSTOMER);
        return review != null && review.getReviewRating() != null ? review.getReviewRating() : null;
    }

    /**
     * 수요자별 예약 통계 조회
     */
    public CustomerReservationStatisticsDto getCustomerReservationStatistics(Long userId) {
        // 수요자 존재 여부 확인
        User user = userRepository.findById(userId)
                .filter(u -> u.getUserRole() == UserRole.CUSTOMER)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 수요자입니다."));

        // 예약 통계 계산
        List<Reservation> customerReservations = reservationRepository.findByCustomer_UserId(userId);
        
        long totalReservations = customerReservations.size();
        long completedReservations = customerReservations.stream()
                .filter(r -> r.getReservationStatus() == ReservationStatus.DONE)
                .count();
        long pendingReservations = customerReservations.stream()
                .filter(r -> r.getReservationStatus() == ReservationStatus.WAITING || 
                           r.getReservationStatus() == ReservationStatus.MATCHING)
                .count();
        long cancelledReservations = customerReservations.stream()
                .filter(r -> r.getReservationStatus() == ReservationStatus.CANCEL)
                .count();

        // 환불 신청 건수 계산
        long refundRequests = refundRepository.countByPayment_Reservation_Customer_UserId(userId);

        // 성공률 계산
        double successRate = totalReservations > 0 ? 
                (double) completedReservations / totalReservations * 100 : 0.0;

        // 평균 만족도 계산 (수요자가 작성한 리뷰의 평균)
        List<Review> writtenReviews = reviewRepository.findByReviewAuthorAndReviewCustomer_UserId(
                ReviewAuthorType.CUSTOMER, userId);
        double averageSatisfaction = writtenReviews.stream()
                .mapToInt(Review::getReviewRating)
                .average()
                .orElse(0.0);

        return CustomerReservationStatisticsDto.builder()
                .totalReservations(totalReservations)
                .completedReservations(completedReservations)
                .pendingReservations(pendingReservations)
                .refundRequests(refundRequests)
                .cancelledReservations(cancelledReservations)
                .successRate(Math.round(successRate * 100.0) / 100.0)
                .averageSatisfaction(Math.round(averageSatisfaction * 100.0) / 100.0)
                .build();
    }

    /**
     * 수요자별 리뷰 정보 조회
     */
    public CustomerReviewInfoDto getCustomerReviewInfo(Long userId) {
        // 수요자 존재 여부 확인
        User user = userRepository.findById(userId)
                .filter(u -> u.getUserRole() == UserRole.CUSTOMER)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 수요자입니다."));

        // 수요자가 작성한 리뷰 (매니저에게)
        List<Review> writtenReviews = reviewRepository.findByReviewAuthorAndReviewCustomer_UserId(
                ReviewAuthorType.CUSTOMER, userId);
        
        // 수요자가 받은 리뷰 (매니저로부터)
        List<Review> receivedReviews = reviewRepository.findByReviewAuthorAndReviewCustomer_UserId(
                ReviewAuthorType.MANAGER, userId);

        // 작성한 리뷰 DTO 변환
        List<CustomerReviewDto> writtenReviewDtos = writtenReviews.stream()
                .map(review -> CustomerReviewDto.builder()
                        .reviewId(review.getReviewId())
                        .rating(review.getReviewRating())
                        .comment(review.getReviewComment())
                        .reviewDate(review.getReviewDate().toString())
                        .targetName(review.getReviewManager().getUserName())
                        .targetProfile(review.getReviewManager().getUserProfile())
                        .build())
                .collect(Collectors.toList());

        // 받은 리뷰 DTO 변환
        List<CustomerReviewDto> receivedReviewDtos = receivedReviews.stream()
                .map(review -> CustomerReviewDto.builder()
                        .reviewId(review.getReviewId())
                        .rating(review.getReviewRating())
                        .comment(review.getReviewComment())
                        .reviewDate(review.getReviewDate().toString())
                        .targetName(review.getReviewManager().getUserName())
                        .targetProfile(review.getReviewManager().getUserProfile())
                        .build())
                .collect(Collectors.toList());

        // 평균 평점 계산
        double averageWrittenRating = writtenReviews.stream()
                .mapToInt(Review::getReviewRating)
                .average()
                .orElse(0.0);

        double averageReceivedRating = receivedReviews.stream()
                .mapToInt(Review::getReviewRating)
                .average()
                .orElse(0.0);

        return CustomerReviewInfoDto.builder()
                .writtenReviews(writtenReviewDtos)
                .receivedReviews(receivedReviewDtos)
                .totalWrittenReviews((long) writtenReviews.size())
                .totalReceivedReviews((long) receivedReviews.size())
                .averageWrittenRating(Math.round(averageWrittenRating * 100.0) / 100.0)
                .averageReceivedRating(Math.round(averageReceivedRating * 100.0) / 100.0)
                .build();
    }

    /**
     * 매니저별 리뷰 정보 조회
     */
    public ManagerReviewInfoDto getManagerReviewInfo(Long userId) {
        // 매니저 존재 여부 확인
        User user = userRepository.findById(userId)
                .filter(u -> u.getUserRole() == UserRole.MANAGER)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매니저입니다."));

        // 매니저가 받은 리뷰 (고객으로부터)
        List<Review> receivedReviews = reviewRepository.findByReviewAuthorAndReviewManager_UserId(
                ReviewAuthorType.CUSTOMER, userId);
        
        // 매니저가 작성한 리뷰 (고객에게)
        List<Review> writtenReviews = reviewRepository.findByReviewAuthorAndReviewManager_UserId(
                ReviewAuthorType.MANAGER, userId);

        // 받은 리뷰 DTO 변환
        List<CustomerReviewDto> receivedReviewDtos = receivedReviews.stream()
                .map(review -> CustomerReviewDto.builder()
                        .reviewId(review.getReviewId())
                        .rating(review.getReviewRating())
                        .comment(review.getReviewComment())
                        .reviewDate(review.getReviewDate().toString())
                        .targetName(review.getReviewCustomer().getUserName())
                        .targetProfile(review.getReviewCustomer().getUserProfile())
                        .build())
                .collect(Collectors.toList());

        // 작성한 리뷰 DTO 변환
        List<CustomerReviewDto> writtenReviewDtos = writtenReviews.stream()
                .map(review -> CustomerReviewDto.builder()
                        .reviewId(review.getReviewId())
                        .rating(review.getReviewRating())
                        .comment(review.getReviewComment())
                        .reviewDate(review.getReviewDate().toString())
                        .targetName(review.getReviewCustomer().getUserName())
                        .targetProfile(review.getReviewCustomer().getUserProfile())
                        .build())
                .collect(Collectors.toList());

        // 평균 평점 계산
        double avgReceived = receivedReviews.stream()
                .mapToInt(Review::getReviewRating)
                .average()
                .orElse(0.0);

        double avgWritten = writtenReviews.stream()
                .mapToInt(Review::getReviewRating)
                .average()
                .orElse(0.0);

        BigDecimal averageWrittenRating = BigDecimal.valueOf(avgWritten).setScale(2, RoundingMode.HALF_UP);
        BigDecimal averageReceivedRating = BigDecimal.valueOf(avgReceived).setScale(2, RoundingMode.HALF_UP);


        return ManagerReviewInfoDto.builder()
                .receivedReviews(receivedReviewDtos)
                .writtenReviews(writtenReviewDtos)
                .totalReceivedReviews(receivedReviews.size())
                .totalWrittenReviews(writtenReviews.size())
                .averageReceivedRating(averageReceivedRating)
                .averageWrittenRating(averageWrittenRating)
                .build();
    }

    private AdminUserResponseDto convertToDto(User user) {
        
        return AdminUserResponseDto.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .userEmail(user.getUserEmail())
                .userTel(user.getUserTel())
                .userCreatedDate(user.getUserCreatedAt().toString())
                .approvedAt(user.getManagerStatus() != null && user.getManagerStatus().equals("APPROVED") ? 
                        user.getUserCreatedAt().toString() : null)
                .userGender(user.getUserGender() == UserGender.M ? "남성" : "여성")
                .userBirth(user.getUserBirth().toString())
                .userProfile(user.getUserProfile())
                .isBlack(user.getIsBlack())
                .blacklistReason(user.getBlacklistReason())
                .blacklistDate(user.getBlacklistDate() != null ? 
                        user.getBlacklistDate().toString() : null)
                .build();
    }

} 