package com.antmen.antwork.common.api.request.account;

import com.antmen.antwork.common.domain.entity.account.UserGender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
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

    private MultipartFile userProfile;

}
