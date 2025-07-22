package com.antmen.antwork.domain.user.dto;

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

    private Long userId;

    private String userName;

    private String userTel;

    private String userEmail;

    private String userGender;

    private LocalDate userBirth;

    private String userProfile;

    private Integer customerPoint;
}
