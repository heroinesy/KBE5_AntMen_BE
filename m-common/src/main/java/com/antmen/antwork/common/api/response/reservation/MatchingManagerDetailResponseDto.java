package com.antmen.antwork.common.api.response.reservation;

import com.antmen.antwork.common.domain.entity.ReviewSummary;
import com.antmen.antwork.common.domain.entity.account.ManagerDetail;
import com.antmen.antwork.common.domain.entity.account.User;
import com.antmen.antwork.common.domain.entity.account.UserGender;
import com.antmen.antwork.common.domain.entity.reservation.Review;
import lombok.*;

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
    private Float rating;
    private Long reviewCount;
    private String introduction;
    private List<Review> reviewList = null;
    // TODO: 성격 특징 들어가야함.

    public MatchingManagerDetailResponseDto toDto(User user, ManagerDetail managerDetail, ReviewSummary reviewSummary) {
        return MatchingManagerDetailResponseDto.builder()
                .profileImage(user.getUserProfile())
                .name(user.getUserName())
                .gender(user.getUserGender() == UserGender.M? "남성": "여성")
                .age(user.getUserBirth().until(LocalDate.now()).getYears())
                .rating(reviewSummary.getAvgRating())
                .reviewCount(reviewSummary.getTotalReviews())
                // TODO: DB수정 필요
                .introduction("열심을 다하는 친절한 엔트워커입니다")
                .reviewList(null) // TODO: 리뷰 작업할 때 넣기
                .build();
    }
}
