package com.antmen.antwork.common.api.request.account;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ManagerInfoUpdateRequestDto {

    private String userName;

    private String userTel;

    private String userEmail;

    private LocalDate userBirth;

    private String managerAddress;

    private Double managerLatitude;

    private Double managerLongitude;

    private String managerTime;

    private String userType;

}
