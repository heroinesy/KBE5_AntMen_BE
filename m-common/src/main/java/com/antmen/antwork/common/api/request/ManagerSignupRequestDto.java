package com.antmen.antwork.common.api.request;

import com.antmen.antwork.common.domain.entity.UserGender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ManagerSignupRequestDto {

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

    @NotNull(message = "프로필 사진은 필수입니다.")
    private MultipartFile userProfile;

    @NotBlank
    private String managerAddress;

    @NotBlank
    private String managerArea;

    @NotBlank
    private String managerTime;

    @Size(min = 1, message = "최소 1개의 신원 파일이 필요합니다.")
    private List<MultipartFile> managerFileUrls = new ArrayList<>();
}
