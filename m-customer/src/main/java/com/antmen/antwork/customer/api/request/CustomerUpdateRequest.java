package com.antmen.antwork.customer.api.request;

import com.antmen.antwork.common.domain.entity.UserGender;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true) // 없는 필드는 자동무시
public class CustomerUpdateRequest {

    private String token;

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
