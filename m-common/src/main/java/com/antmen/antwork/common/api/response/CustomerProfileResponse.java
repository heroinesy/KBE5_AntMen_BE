package com.antmen.antwork.common.api.response;

import com.antmen.antwork.common.domain.entity.UserGender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerProfileResponse {

    private String userName;

    private String userTel;

    private String userEmail;

    private UserGender userGender;

    private LocalDate userBirth;

    //    private MultipartFile userProfile;
    private String userProfile;

    private Integer customerPoint;
}
