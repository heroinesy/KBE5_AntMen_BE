package com.antmen.antwork.domain.user.dto;














@Getter
@Setter
@NoArgsConstructor
public class ManagerSignupRequestDto {

    @NotBlank
    private String userLoginId;

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
    private Double managerLatitude;

    @NotBlank
    private Double managerLongitude;

    @NotBlank
    private String managerTime;

    @Size(min = 1, message = "최소 1개의 신원 파일이 필요합니다.")
    private List<MultipartFile> managerFileUrls = new ArrayList<>();

    private String userType;
}
