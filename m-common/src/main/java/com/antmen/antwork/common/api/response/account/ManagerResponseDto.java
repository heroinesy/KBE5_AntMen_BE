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
    private Long userId;
    private String userName;
    private String userTel;
    private String userEmail;
    private String userGender;
    private LocalDate userBirth;
    private String userProfile;
    private String managerAddress;
    private String managerArea;     // TODO: 위치정보 적용하면 빼기
    private String managerTime;     // TODO: 요일, 시간 분리
    private List<ManagerIdFileDto> managerFileUrls;
    private ManagerStatus managerStatus;
    private String rejectReason;
}
