package com.antmen.antwork.common.api.request;

import com.antmen.antwork.common.domain.entity.UserGender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerSignupRequest {

    @NotBlank
    private String userLoginId;

    @NotBlank
    private String userPassword;

    @NotBlank
    private String userName;

    @NotBlank
    private String userTel;

    @NotBlank
    private String userEmail;

    @NotNull
    private UserGender userGender;

    @NotNull
    private LocalDate userBirth;

//    private MultipartFile userProfile;
    @NotNull
    private String userProfile;

}
