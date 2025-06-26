package com.antmen.antwork.common.api.request.account;

import com.antmen.antwork.common.domain.entity.account.UserGender;
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
public class ManagerUpdateRequestDto {

    private String userName;

    private String userTel;

    private String userEmail;

    private UserGender userGender;

    private LocalDate userBirth;

    @NotNull(message = "프로필 사진은 필수입니다.")
    private MultipartFile userProfile;

    private String managerAddress;

    private Double managerLatitude;

    private Double managerLongitude;

    private String managerTime;

    @Size(min = 1, message = "최소 1개의 신원 파일이 필요합니다.")
    private List<MultipartFile> managerFileUrls = new ArrayList<>();

    private String userType;

}
