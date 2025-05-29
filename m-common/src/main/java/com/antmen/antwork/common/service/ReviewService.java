package com.antmen.antwork.common.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.antmen.antwork.common.api.request.ReviewRequest;
import com.antmen.antwork.common.api.request.ReviewRequestDto;
import com.antmen.antwork.common.api.response.ReviewResponse;
import com.antmen.antwork.common.api.response.ReviewResponseDto;
import com.antmen.antwork.common.domain.entity.Reservation;
import com.antmen.antwork.common.domain.entity.Review;
import com.antmen.antwork.common.domain.entity.ReviewStatus;
import com.antmen.antwork.common.domain.entity.User;
import com.antmen.antwork.common.domain.entity.UserRole;
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

    /**
     * 예림님 작성중
     */
    public ReviewResponse writeReview(
            Long loginId,
            ReviewRequest reviewRequest) {

        // 예약한 내역이 있는지 확인
        Reservation reservation = reservationRepository.findById(reviewRequest.getReservationId())
                .orElseThrow(() -> new NotFoundException("예약이 존재하지 않습니다."));

        User user = userRepository.findById(loginId)
                .orElseThrow(() -> new NotFoundException("회원이 존재하지 않습니다."));

        // 예약한 날짜와 시간이 지난 후 리뷰 작성 가능하게 해야함

        // 이미 같은 방향의 리뷰가 존재하는지 검사

        Review review = reviewMapper.toEntity(reservation, reviewRequest);

        if(user.getUserRole().equals(UserRole.Customer)){ // 로그인한 유저가 수요자라면

            // 내가 예약한 것이 맞는지 확인
            if(reservation.getCustomer().getUserId().equals(loginId)) {
                review.setReviewStatus(ReviewStatus.CUSTOMER);
            } else {
                throw new RuntimeException("리뷰 작성 권한이 없습니다."); // exception 수정해야함
            }

        } else {
            if(reservation.getManager().getUserId().equals(loginId)) {
                review.setReviewStatus(ReviewStatus.MANAGER);
            } else {
                throw new RuntimeException("근무 내역이 아닙니다."); // exception 수정해야함
            }
        }

        reviewRepository.save(review);

        return reviewMapper.toDto(review);
    }

    @Transactional
    public ReviewResponseDto createReview(ReviewRequestDto dto) {
        User customer = userRepository.findById(dto.getReviewCustomerId())
                .orElseThrow(() -> new NotFoundException("고객을 찾을 수 없습니다."));
        User manager = userRepository.findById(dto.getReviewManagerId())
                .orElseThrow(() -> new NotFoundException("매니저를 찾을 수 없습니다."));
        Reservation reservation = reservationRepository.findById(dto.getReservationId())
                .orElseThrow(() -> new NotFoundException("예약을 찾을 수 없습니다."));
        Review review = reviewMapper.toEntity(dto, customer, manager, reservation);
        Review saved = reviewRepository.save(review);
        return reviewMapper.toDto(saved);
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

    @Transactional
    public ReviewResponseDto updateReview(Long reviewId, ReviewRequestDto dto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("리뷰를 찾을 수 없습니다."));
        review.setReviewRating(dto.getReviewRating());
        review.setReviewComment(dto.getReviewComment());
        review.setReviewAuThor(dto.getReviewAuthor());
        Review saved = reviewRepository.save(review);
        return reviewMapper.toDto(saved);
    }

    // 본인이 작성한 글인지 확인필요 
    @Transactional
    public void deleteReview(Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new NotFoundException("리뷰를 찾을 수 없습니다.");
        }
        reviewRepository.deleteById(reviewId);
    }
}
