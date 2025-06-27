package com.antmen.antwork.common.api.response.account;

import com.antmen.antwork.common.domain.entity.account.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class UserListResponseDto {
    private String userName;
    private String userEmail;
    private String userTel;
    private LocalDateTime userCreatedDate;
    private LocalDateTime lastLoginDate;
    private LocalDateTime lastReservationDate;

    public static UserListResponseDto toListDto(User user) {
        return UserListResponseDto.builder()
                .userName(user.getUserName())
                .userEmail(user.getUserEmail())
                .userTel(user.getUserTel())
                .userCreatedDate(user.getUserCreatedAt())
                .lastLoginDate(user.getLastLoginAt())
                .lastReservationDate(user.getLastReservationAt())
                .build();
    }
}
