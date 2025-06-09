package com.antmen.antwork.common.api.response.reservation;

import com.antmen.antwork.common.domain.entity.account.User;
import com.antmen.antwork.common.domain.entity.account.UserGender;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter
@Builder
public class MatchingManagerListResponseDto {
    private Long managerId;
    private String managerName;
    private String managerGender;
    private Integer managerAge;
    private String managerComment;
    private double managerRating;
    private String managerImage;

    // TODO: 매개변수 더 추가될 것으로 예상
    public static MatchingManagerListResponseDto toDto(User user) {
        return MatchingManagerListResponseDto.builder()
                .managerId(user.getUserId())
                .managerName(user.getUserName())
                .managerGender(user.getUserGender() == UserGender.M ? "남성" : "여성")
                .managerAge(Period.between(user.getUserBirth(), LocalDate.now()).getYears())
                // TODO: 매니저 가입 과정 or 회원정보 수정 로직 중 소개글 작성 부분 생성 필요
                .managerComment("")
                // TODO: 타 테이블과 연동 필요
                .managerRating(0.0)
                .managerImage(user.getUserProfile())
                .build();
    }
}
