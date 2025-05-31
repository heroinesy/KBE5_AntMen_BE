package com.antmen.antwork.common.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.antmen.antwork.common.domain.entity.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.antmen.antwork.common.api.request.ReviewRequestDto;
import com.antmen.antwork.common.api.response.ReviewResponseDto;
import com.antmen.antwork.common.domain.exception.NotFoundException;
import com.antmen.antwork.common.infra.repository.ReservationRepository;
import com.antmen.antwork.common.infra.repository.ReviewRepository;
import com.antmen.antwork.common.infra.repository.UserRepository;
import com.antmen.antwork.common.service.mapper.ReviewMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final ReviewMapper reviewMapper;

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

        review.setReviewRating(dto.getReviewRating());
        review.setReviewComment(dto.getReviewComment());

        return reviewMapper.toDto(reviewRepository.save(review));
    }

    @Transactional
    public void deleteReview(Long loginId, Long reviewId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("리뷰를 찾을 수 없습니다."));

        validateReviewAuthor(review, loginId);

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
}


