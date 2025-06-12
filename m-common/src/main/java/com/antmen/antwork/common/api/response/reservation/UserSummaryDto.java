package com.antmen.antwork.common.api.response.reservation;

import com.antmen.antwork.common.domain.entity.account.User;
import com.antmen.antwork.common.domain.entity.account.UserGender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.Period;

@Getter
@Builder
@AllArgsConstructor
public class UserSummaryDto {
    private Long userId;
    private String name;
    private String gender;
    private int age;
    private String profileImage;

    public static UserSummaryDto from(User user) {
        return UserSummaryDto.builder()
                .userId(user.getUserId())
                .name(user.getUserName())
                .gender(user.getUserGender() == UserGender.M ? "남성" : "여성")
                .age(Period.between(user.getUserBirth(), LocalDate.now()).getYears())
                .profileImage(user.getUserProfile())
                .build();
    }
}