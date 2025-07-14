package com.antmen.antwork.admin.service;

import com.antmen.antwork.admin.api.AdminReviewStatisticsDto;
import com.antmen.antwork.admin.api.ReviewSatisfactionDto;
import com.antmen.antwork.common.domain.entity.ReviewSummary;
import com.antmen.antwork.common.domain.entity.account.UserRole;
import com.antmen.antwork.common.infra.repository.account.UserRepository;
import com.antmen.antwork.common.infra.repository.reservation.ReviewSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminReviewStatisticService {
    private final ReviewSummaryRepository reviewSummaryRepository;
    private final UserRepository userRepository;

    public AdminReviewStatisticsDto getReviewStatistics(int topN) {
        List<ReviewSummary> allSummaries = reviewSummaryRepository.findAll();
        Long totalCount = allSummaries.stream().mapToLong(ReviewSummary::getTotalReviews).sum();

        BigDecimal avg = allSummaries.stream()
                .map(summary -> summary.getAvgRating()
                        .multiply(BigDecimal.valueOf(summary.getTotalReviews())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(totalCount), 2, BigDecimal.ROUND_HALF_UP);

        BigDecimal avgCustomerScore = calcAvgByRole(allSummaries, UserRole.CUSTOMER);
        BigDecimal avgManagerScore = calcAvgByRole(allSummaries, UserRole.MANAGER);

        List<ReviewSatisfactionDto> topManagers = reviewSummaryRepository
                .findTopByRole(UserRole.MANAGER, PageRequest.of(0, topN))
                .stream()
                .map(this::toDto)
                .toList();

        List<ReviewSatisfactionDto> topCustomers = reviewSummaryRepository
                .findTopByRole(UserRole.CUSTOMER, PageRequest.of(0, topN))
                .stream()
                .map(this::toDto)
                .toList();

        return AdminReviewStatisticsDto.builder()
                .totalReviewCount(totalCount)
                .avgReviewSatisfaction(avg)
                .avgCustomerReviewSatisfaction(avgCustomerScore)
                .avgManagerReviewSatisfaction(avgManagerScore)
                .topManagerList(topManagers)
                .topCustomerList(topCustomers)
                .build();
    }

    private ReviewSatisfactionDto toDto(ReviewSummary reviewSummary) {
        return ReviewSatisfactionDto.builder()
                .userId(reviewSummary.getUserId())
                .userName(reviewSummary.getUser().getUserName())
                .avgReview(reviewSummary.getAvgRating())
                .totalReviewCount(reviewSummary.getTotalReviews())
                .build();
    }

    BigDecimal calcAvgByRole(List<ReviewSummary> summaries, UserRole role) {
        List<ReviewSummary> filtered = summaries.stream()
                .filter(rs -> rs.getRole() == role)
                .toList();

        Long totalCount = filtered.stream()
                .mapToLong(ReviewSummary::getTotalReviews)
                .sum();

        if (totalCount == 0) return BigDecimal.ZERO;

        BigDecimal totalScoreSum = filtered.stream()
                .map(rs -> rs.getAvgRating().multiply(BigDecimal.valueOf(rs.getTotalReviews())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalScoreSum.divide(BigDecimal.valueOf(totalCount), 2, RoundingMode.HALF_UP);
    }
}
