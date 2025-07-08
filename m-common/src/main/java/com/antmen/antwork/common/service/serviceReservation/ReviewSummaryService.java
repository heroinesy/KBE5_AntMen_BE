package com.antmen.antwork.common.service.serviceReservation;

import com.antmen.antwork.common.domain.entity.ReviewSummary;
import com.antmen.antwork.common.domain.entity.account.User;
import com.antmen.antwork.common.domain.entity.account.UserRole;
import com.antmen.antwork.common.domain.entity.reservation.Reservation;
import com.antmen.antwork.common.domain.entity.reservation.ReviewAuthorType;
import com.antmen.antwork.common.infra.repository.reservation.ReviewSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class ReviewSummaryService {
    private final ReviewSummaryRepository reviewSummaryRepository;

    // reviewSummary 갱신
    @Transactional
    public void create(Reservation reservation, ReviewAuthorType authorType, int rating) {
        ReviewSummary summary = getOrCreateSummary(reservation, authorType);
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

    @Transactional
    public void update(Reservation reservation, ReviewAuthorType authorType, int oldRating, int newRating) {
        ReviewSummary summary = getOrCreateSummary(reservation, authorType);
        int updatedScore = summary.getTotalScore() - oldRating + newRating;
        long totalReviews = summary.getTotalReviews(); // 리뷰 수는 그대로

        BigDecimal avg = BigDecimal.valueOf(updatedScore)
                .divide(BigDecimal.valueOf(totalReviews), 2, RoundingMode.HALF_UP);

        summary.setTotalScore(updatedScore);
        summary.setAvgRating(avg);

        reviewSummaryRepository.save(summary);
    }

    @Transactional
    public void delete(Reservation reservation, ReviewAuthorType authorType, int deletedRating) {
        ReviewSummary summary = getOrCreateSummary(reservation,  authorType);
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

    private ReviewSummary getOrCreateSummary(Reservation reservation, ReviewAuthorType authorType) {
        User targetUser = authorType.getAuthorUser(reservation);
        UserRole role = authorType.getUserRole();

        return reviewSummaryRepository.findByUserIdAndRole(targetUser.getUserId(), role)
                .orElseGet(() -> {
                    ReviewSummary newSummary = ReviewSummary.builder()
                            .userId(targetUser.getUserId())
                            .role(role)
                            .totalScore(0)
                            .totalReviews(0L)
                            .avgRating(BigDecimal.ZERO)
                            .build();
                    return reviewSummaryRepository.save(newSummary);
                });
    }
}