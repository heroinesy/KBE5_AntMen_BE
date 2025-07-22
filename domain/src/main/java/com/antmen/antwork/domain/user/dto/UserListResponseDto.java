package com.antmen.antwork.domain.user.dto;

import com.antmen.antwork.domain.user.entity.User;
import com.antmen.antwork.domain.user.entity.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class UserListResponseDto {
    private Long userId;
    private String userName;
    private String userEmail;
    private String userTel;
    private UserRole userRole;
    private LocalDateTime userCreatedDate;
    private LocalDateTime lastReservationDate;
    private Boolean isBlack;
    private String blacklistReason;
    private LocalDateTime blacklistDate;

    public static UserListResponseDto toListDto(User user) {
        return UserListResponseDto.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .userEmail(user.getUserEmail())
                .userTel(user.getUserTel())
                .userRole(user.getUserRole())
                .userCreatedDate(user.getUserCreatedAt())
                .lastReservationDate(user.getLastReservationAt())
                .isBlack(user.getIsBlack())
                .blacklistReason(user.getBlacklistReason())
                .blacklistDate(user.getBlacklistDate())
                .build();
    }
}
