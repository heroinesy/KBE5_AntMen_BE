package com.antmen.antwork.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserResponseDto {
    private Long userId;
    private String userName;
    private String userEmail;
    private String userTel;
    private String userCreatedDate;
    private String userGender;
    private String userBirth;
    private String userProfile;
    private Boolean isBlack;
    private String blacklistReason;
    private String blacklistDate;
} 