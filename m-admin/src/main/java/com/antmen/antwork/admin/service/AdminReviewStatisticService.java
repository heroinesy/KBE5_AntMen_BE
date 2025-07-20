package com.antmen.antwork.admin.service;

import com.antmen.antwork.admin.api.AdminReviewStatisticsDto;
import com.antmen.antwork.admin.api.ReviewSatisfactionDto;
import com.antmen.antwork.domain.review.entity.ReviewSummary;
import com.antmen.antwork.common.domain.entity.account.UserRole;
import com.antmen.antwork.domain.review.repository.ReviewSummaryRepository;
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

    public AdminReviewStatisticsDto getReviewStatistics(int topN) {
        List<ReviewSummary> allSummaries = reviewSummaryRepository.findAll();
        long totalCount = allSummaries.stream().mapToLong(ReviewSummary::getTotalReviews).sum();

        BigDecimal avg = allSummaries.stream()
                .map(summary -> summary.getAvgRating()
                        .multiply(BigDecimal.valueOf(summary.getTotalReviews())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(totalCount), 2, RoundingMode.HALF_UP);

        BigDecimal avgCustomerScore = calcAvgByRole(allSummaries, UserRole.CUSTOMER);
        BigDecimal avgManagerScore = calcAvgByRole(allSummaries, UserRole.MANAGER);

        // 평점 기준
        List<ReviewSatisfactionDto> topManagersByRating = toDtoList(
                reviewSummaryRepository.findTopByAvgRating(UserRole.MANAGER, PageRequest.of(0, topN)));
        List<ReviewSatisfactionDto> topCustomersByRating = toDtoList(
                reviewSummaryRepository.findTopByAvgRating(UserRole.CUSTOMER, PageRequest.of(0, topN)));

        // 개수 기준
        List<ReviewSatisfactionDto> topManagersByCount = toDtoList(
                reviewSummaryRepository.findTopByTotalReviews(UserRole.MANAGER, PageRequest.of(0, topN)));
        List<ReviewSatisfactionDto> topCustomersByCount = toDtoList(
                reviewSummaryRepository.findTopByTotalReviews(UserRole.CUSTOMER, PageRequest.of(0, topN)));

        return AdminReviewStatisticsDto.builder()
                .totalReviewCount(totalCount)
                .avgReviewSatisfaction(avg)
                .avgCustomerReviewSatisfaction(avgCustomerScore)
                .avgManagerReviewSatisfaction(avgManagerScore)

                .topManagerList(topManagersByRating)
                .topCustomerList(topCustomersByRating)
                .topManagerByReviewCount(topManagersByCount)
                .topCustomerByReviewCount(topCustomersByCount)
                .build();
    }

    private List<ReviewSatisfactionDto> toDtoList(List<ReviewSummaryRepository.ReviewerSatisfactionProjection> projections) {
        return projections.stream()
                .map(p -> ReviewSatisfactionDto.builder()
                        .userId(p.getUserId())
                        .userName(p.getName())
                        .avgReview(p.getAvgRating())
                        .totalReviewCount(p.getTotalReviews())
                        .build())
                .toList();
    }

    BigDecimal calcAvgByRole(List<ReviewSummary> summaries, UserRole role) {
        List<ReviewSummary> filtered = summaries.stream()
                .filter(rs -> rs.getRole() == role)
                .toList();

        long totalCount = filtered.stream()
                .mapToLong(ReviewSummary::getTotalReviews)
                .sum();

        if (totalCount == 0) return BigDecimal.ZERO;

        BigDecimal totalScoreSum = filtered.stream()
                .map(rs -> rs.getAvgRating().multiply(BigDecimal.valueOf(rs.getTotalReviews())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalScoreSum.divide(BigDecimal.valueOf(totalCount), 2, RoundingMode.HALF_UP);
    }
}