package com.antmen.antwork.common.api.response;

import com.antmen.antwork.common.domain.entity.UserGender;
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
    private UserGender userGender;
    private LocalDate userBirth;
    private String userProfile;
    private String managerAddress;
    private String managerArea;
    private String managerTime;
    private List<ManagerIdFileDto> managerFileUrls;
}
