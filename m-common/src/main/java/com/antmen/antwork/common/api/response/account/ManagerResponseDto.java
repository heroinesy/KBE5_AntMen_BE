package com.antmen.antwork.common.api.response.account;

import com.antmen.antwork.common.domain.entity.account.ManagerStatus;
import com.antmen.antwork.common.domain.entity.account.UserGender;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class ManagerResponseDto {
    private String userName;
    private String userTel;
    private String userEmail;
    private String userGender;
    private LocalDate userBirth;
    private String userProfile;
    private String managerAddress;
    // 보통 정보 조회용으로 쓰는데 얘까지 보낼 필요가 있는지 의문
//    private Double managerLatitude;
//    private Double managerLongitude;
    private String managerTime;
    private List<ManagerIdFileDto> managerFileUrls;
    private ManagerStatus managerStatus;
    private String rejectReason;
    private String userType;
}
