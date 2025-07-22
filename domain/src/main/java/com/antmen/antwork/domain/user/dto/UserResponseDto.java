package com.antmen.antwork.domain.user.dto;

import com.antmen.antwork.domain.user.entity.User;
import com.antmen.antwork.domain.user.entity.UserGender;
import com.antmen.antwork.domain.user.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class UserResponseDto {

    private Long userId;
    private String userLoginId;
    private String userName;
    private String userTel;
    private String userEmail;
    private UserRole userRole;
    private UserGender userGender;
    private LocalDate userBirth;
    private String userProfile;
    private String userType;
    private LocalDateTime userCreatedAt;

    public static UserResponseDto from(User user) {
        return UserResponseDto.builder()
                .userId(user.getUserId())
                .userLoginId(user.getUserLoginId())
                .userName(user.getUserName())
                .userTel(user.getUserTel())
                .userEmail(user.getUserEmail())
                .userRole(user.getUserRole())
                .userGender(user.getUserGender())
                .userBirth(user.getUserBirth())
                .userProfile(user.getUserProfile())
                .userType(user.getUserType())
                .userCreatedAt(user.getUserCreatedAt())
                .build();
    }
}
