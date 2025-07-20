package com.antmen.antwork.domain.matching.dto;

import com.antmen.antwork.common.domain.entity.ReviewSummary;
import com.antmen.antwork.common.domain.entity.account.ManagerDetail;
import com.antmen.antwork.common.domain.entity.account.User;
import com.antmen.antwork.common.domain.entity.account.UserGender;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchingManagerDetailResponseDto {
    private String profileImage;
    private String name;
    private String gender;
    private Integer age;
    private BigDecimal rating;
    private Long reviewCount;
    private String introduction;
    private List<ReviewResponseDto> reviewList;
    // TODO: 성격 특징 들어가야함.

    public MatchingManagerDetailResponseDto toDto(User user, ManagerDetail detail, ReviewSummary reviewSummary, List<ReviewResponseDto> reviewList) {
        return MatchingManagerDetailResponseDto.builder()
                .profileImage(user.getUserProfile())
                .name(user.getUserName())
                .gender(user.getUserGender() == UserGender.M? "남성": "여성")
                .age(user.getUserBirth().until(LocalDate.now()).getYears())
                .rating(reviewSummary != null ? reviewSummary.getAvgRating() : BigDecimal.ZERO)
                .reviewCount(reviewSummary != null ? reviewSummary.getTotalReviews() : 0L)
                .introduction("열심을 다하는 친절한 엔트워커입니다")
                .reviewList(reviewList)
                .build();
    }
}