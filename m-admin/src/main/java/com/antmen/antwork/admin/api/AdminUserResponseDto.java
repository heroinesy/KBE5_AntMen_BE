package com.antmen.antwork.admin.api;

import com.antmen.antwork.common.api.response.CustomerReviewDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

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
    private String approvedAt;
    private String userGender;
    private String userBirth;
    private String userProfile;
    private Boolean isBlack;
    private String blacklistReason;
    private String blacklistDate;
} 