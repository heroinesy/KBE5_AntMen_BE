package com.antmen.antwork.domain.user.dto;












@Getter
@Builder
@AllArgsConstructor
public class UserSummaryDto {
    private Long userId;
    private String name;
    private String gender;
    private int age;
    private String profileImage;
    private Long totalReviews;
    private BigDecimal avgRating;

    public static UserSummaryDto from(User user, ReviewSummary reviewSummary) {
        return UserSummaryDto.builder()
                .userId(user.getUserId())
                .name(user.getUserName())
                .gender(user.getUserGender() == UserGender.M ? "남성" : "여성")
                .age(Period.between(user.getUserBirth(), LocalDate.now()).getYears())
                .profileImage(user.getUserProfile())
                .totalReviews(reviewSummary != null ? reviewSummary.getTotalReviews() : 0L)
                .avgRating(reviewSummary != null ? reviewSummary.getAvgRating() : BigDecimal.ZERO)
                .build();
    }
}