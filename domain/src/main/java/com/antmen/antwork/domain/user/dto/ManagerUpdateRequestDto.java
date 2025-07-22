package com.antmen.antwork.domain.user.dto;













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
