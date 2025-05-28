package com.antmen.antwork.common.service;

import com.antmen.antwork.common.api.request.ReviewRequestDto;
import com.antmen.antwork.common.api.response.ReviewResponseDto;
import com.antmen.antwork.common.domain.entity.Review;
import com.antmen.antwork.common.domain.entity.User;
import com.antmen.antwork.common.domain.entity.Reservation;
import com.antmen.antwork.common.domain.entity.ReviewAuthorType;
import com.antmen.antwork.common.domain.exception.NotFoundException;
import com.antmen.antwork.common.infra.repository.ReviewRepository;
import com.antmen.antwork.common.infra.repository.UserRepository;
import com.antmen.antwork.common.infra.repository.ReservationRepository;
import com.antmen.antwork.common.service.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final ReviewMapper reviewMapper;

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

    @Transactional
    public void deleteReview(Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new NotFoundException("리뷰를 찾을 수 없습니다.");
        }
        reviewRepository.deleteById(reviewId);
    }
} 