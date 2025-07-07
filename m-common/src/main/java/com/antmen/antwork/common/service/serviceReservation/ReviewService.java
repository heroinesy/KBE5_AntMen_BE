package com.antmen.antwork.common.service.serviceReservation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.antmen.antwork.common.domain.entity.ReviewSummary;
import com.antmen.antwork.common.domain.entity.account.User;
import com.antmen.antwork.common.domain.entity.account.UserRole;
import com.antmen.antwork.common.domain.entity.reservation.Reservation;
import com.antmen.antwork.common.domain.entity.reservation.Review;
import com.antmen.antwork.common.domain.entity.reservation.ReviewAuthorType;
import com.antmen.antwork.common.infra.repository.reservation.ReviewSummaryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.antmen.antwork.common.api.request.reservation.ReviewRequestDto;
import com.antmen.antwork.common.api.response.reservation.ReviewResponseDto;
import com.antmen.antwork.common.domain.exception.NotFoundException;
import com.antmen.antwork.common.infra.repository.reservation.ReservationRepository;
import com.antmen.antwork.common.infra.repository.reservation.ReviewRepository;
import com.antmen.antwork.common.infra.repository.account.UserRepository;
import com.antmen.antwork.common.service.mapper.reservation.ReviewMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final ReviewMapper reviewMapper;
    private final ReviewSummaryRepository reviewSummaryRepository;

    @Transactional
    public ReviewResponseDto createReview(Long loginId, ReviewRequestDto dto) {

        // 리뷰 작성 가능 시간 검증
        // 리뷰 중복 검사
        Reservation reservation = reservationRepository.findById(dto.getReservationId())
                .orElseThrow(() -> new NotFoundException("예약을 찾을 수 없습니다."));
        User customer = userRepository.findById(reservation.getCustomer().getUserId())
                .orElseThrow(() -> new NotFoundException("고객을 찾을 수 없습니다."));
        User manager = userRepository.findById(reservation.getManager().getUserId())
                .orElseThrow(() -> new NotFoundException("매니저를 찾을 수 없습니다."));

        if (dto.getReviewAuthor() == ReviewAuthorType.CUSTOMER) {
            if (!customer.getUserId().equals(loginId)) {
                throw new RuntimeException("본인의 예약만 리뷰를 작성할 수 있습니다."); // exception 수정 필요
            }
        } else if (dto.getReviewAuthor() == ReviewAuthorType.MANAGER) {
            if (!manager.getUserId().equals(loginId)) {
                throw new RuntimeException("본인의 예약만 리뷰를 작성할 수 있습니다."); // exception 수정 필요
            }
        }
        Review review = reviewMapper.toEntity(dto, customer, manager, reservation);
        updateReviewSummary(reservation, dto.getReviewAuthor(), dto.getReviewRating());
        return reviewMapper.toDto(reviewRepository.save(review));
    }

    @Transactional(readOnly = true)
    public ReviewResponseDto getReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("리뷰를 찾을 수 없습니다."));
        return reviewMapper.toDto(review);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(reviewMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getMyReceivedReviews(Long loginId) {

        User user = userRepository.findById(loginId)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        List<Review> reviews = new ArrayList<>();

        if (user.getUserRole() == UserRole.CUSTOMER) {
            reviews = reviewRepository.findByReviewAuthorAndReviewCustomer_UserId(ReviewAuthorType.MANAGER, loginId);
        } else if (user.getUserRole() == UserRole.MANAGER) {
            reviews = reviewRepository.findByReviewAuthorAndReviewManager_UserId(ReviewAuthorType.CUSTOMER, loginId);
        }

        return reviews.stream().map(reviewMapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getMyWrittenReviews(Long loginId) {

        User user = userRepository.findById(loginId)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        List<Review> reviews = new ArrayList<>();

        if (user.getUserRole() == UserRole.CUSTOMER) {
            reviews = reviewRepository.findByReviewAuthorAndReviewCustomer_UserId(ReviewAuthorType.CUSTOMER, loginId);
        } else if (user.getUserRole() == UserRole.MANAGER) {
            reviews = reviewRepository.findByReviewAuthorAndReviewManager_UserId(ReviewAuthorType.MANAGER, loginId);
        }

        return reviews.stream().map(reviewMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getReviewsByUserId(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        List<Review> reviews = new ArrayList<>();

        if (user.getUserRole() == UserRole.CUSTOMER) {
            reviews = reviewRepository.findByReviewAuthorAndReviewCustomer_UserId(ReviewAuthorType.MANAGER, userId);
        } else if (user.getUserRole() == UserRole.MANAGER) {
            reviews = reviewRepository.findByReviewAuthorAndReviewManager_UserId(ReviewAuthorType.CUSTOMER, userId);
        }

        return reviews.stream().map(reviewMapper::toDto)
                .collect(Collectors.toList());

    }

    @Transactional
    public ReviewResponseDto updateReview(Long loginId, Long reviewId, ReviewRequestDto dto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("리뷰를 찾을 수 없습니다."));

        validateReviewAuthor(review, loginId);

        short oldRating = review.getReviewRating();
        short newRating = dto.getReviewRating();

        review.setReviewRating(newRating);
        review.setReviewComment(dto.getReviewComment());

        Review savedReview = reviewRepository.save(review);
        updateReviewSummaryUpdate(review.getReservation(), dto.getReviewAuthor(), oldRating, newRating);

        return reviewMapper.toDto(savedReview);
    }

    @Transactional
    public void deleteReview(Long loginId, Long reviewId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("리뷰를 찾을 수 없습니다."));

        validateReviewAuthor(review, loginId);
        updateReviewSummaryDelete(review.getReservation(), review.getReviewAuthor(), review.getReviewRating());

        reviewRepository.delete(review);
    }

    // 리뷰작성자, 로그인id 비교
    private void validateReviewAuthor(Review review, Long loginId) {

        if (review.getReviewAuthor() == ReviewAuthorType.CUSTOMER) {
            if (!review.getReviewCustomer().getUserId().equals(loginId)) {
                throw new RuntimeException("리뷰 작성자만 수정 또는 삭제할 수 있습니다."); // exception 수정 필요
            }
        } else if (review.getReviewAuthor() == ReviewAuthorType.MANAGER) {
            if (!review.getReviewManager().getUserId().equals(loginId)) {
                throw new RuntimeException("리뷰 작성자만 수정 또는 삭제할 수 있습니다."); // exception 수정 필요
            }
        } else {
            throw new IllegalStateException("알 수 없는 리뷰 작성자 유형입니다.");
        }
    }

    public Boolean existsByReservationIdAndAuthorId(Long reservationId, Long loginId) {

        User user = userRepository.findById(loginId)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        if (user.getUserRole() == UserRole.CUSTOMER) {
            return reviewRepository.existsByReservation_ReservationIdAndReviewAuthorAndReviewCustomer_UserId(reservationId,ReviewAuthorType.CUSTOMER,loginId);

        } else if (user.getUserRole() == UserRole.MANAGER) {
            return reviewRepository.existsByReservation_ReservationIdAndReviewAuthorAndReviewManager_UserId(reservationId,ReviewAuthorType.MANAGER,loginId);
        }
        return false;
    }

    // reviewSummary 갱신
    @Transactional
    public void updateReviewSummary(Reservation reservation, ReviewAuthorType authorType, int rating) {
        User targetUser = authorType.getAuthorUser(reservation);
        UserRole role = authorType.getUserRole();

        ReviewSummary summary = reviewSummaryRepository
                .findByUserIdAndRole(targetUser.getUserId(), role)
                .orElseGet(() -> ReviewSummary.builder()
                        .userId(targetUser.getUserId())
                        .role(role)
                        .totalScore(0)
                        .totalReviews(0L)
                        .avgRating(BigDecimal.ZERO)
                        .build());

        int newTotalScore = summary.getTotalScore() + rating;
        long newTotalReviews = summary.getTotalReviews() + 1;
        // 리뷰 점수 합/총 리뷰수 , 소수점 3자리는 반올림
        BigDecimal avg = BigDecimal.valueOf(newTotalScore)
                .divide(BigDecimal.valueOf(newTotalReviews), 2, RoundingMode.HALF_UP);

        summary.setTotalScore(newTotalScore);
        summary.setTotalReviews(newTotalReviews);
        summary.setAvgRating(avg);
        reviewSummaryRepository.save(summary);
    }

    public void updateReviewSummaryUpdate(Reservation reservation, ReviewAuthorType authorType, int oldRating, int newRating) {
        User targetUser = authorType.getAuthorUser(reservation);
        UserRole role = authorType.getUserRole();

        ReviewSummary summary = reviewSummaryRepository.findByUserIdAndRole(targetUser.getUserId(), role)
                .orElseThrow(() -> new IllegalStateException("리뷰 요약 정보가 없습니다."));

        int updatedScore = summary.getTotalScore() - oldRating + newRating;
        long totalReviews = summary.getTotalReviews(); // 리뷰 수는 그대로

        BigDecimal avg = BigDecimal.valueOf(updatedScore)
                .divide(BigDecimal.valueOf(totalReviews), 2, RoundingMode.HALF_UP);

        summary.setTotalScore(updatedScore);
        summary.setAvgRating(avg);

        reviewSummaryRepository.save(summary);
    }

    public void updateReviewSummaryDelete(Reservation reservation, ReviewAuthorType authorType, int deletedRating) {
        User targetUser = authorType.getAuthorUser(reservation);
        UserRole role = authorType.getUserRole();

        ReviewSummary summary = reviewSummaryRepository.findByUserIdAndRole(targetUser.getUserId(), role)
                .orElseThrow(() -> new IllegalStateException("리뷰 요약 정보가 없습니다."));

        int newTotalScore = summary.getTotalScore() - deletedRating;
        long newTotalReviews = summary.getTotalReviews() - 1;

        BigDecimal avg = (newTotalReviews == 0)
                ? BigDecimal.ZERO
                : BigDecimal.valueOf(newTotalScore)
                .divide(BigDecimal.valueOf(newTotalReviews), 2, RoundingMode.HALF_UP);

        summary.setTotalScore(newTotalScore);
        summary.setTotalReviews(newTotalReviews);
        summary.setAvgRating(avg);

        reviewSummaryRepository.save(summary);
    }
}