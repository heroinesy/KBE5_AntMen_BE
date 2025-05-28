package com.antmen.antwork.common.service;

import com.antmen.antwork.common.api.request.ReviewRequest;
import com.antmen.antwork.common.api.response.ReviewResponse;
import com.antmen.antwork.common.domain.entity.*;
import com.antmen.antwork.common.domain.exception.NotFoundException;
import com.antmen.antwork.common.infra.repository.ReservationRepository;
import com.antmen.antwork.common.infra.repository.ReviewRepository;
import com.antmen.antwork.common.infra.repository.UserRepository;
import com.antmen.antwork.common.service.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

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
}
