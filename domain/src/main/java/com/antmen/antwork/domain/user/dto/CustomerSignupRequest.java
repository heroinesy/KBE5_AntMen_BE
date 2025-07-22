package com.antmen.antwork.domain.user.dto;











@Getter
@Setter
@NoArgsConstructor
public class CustomerSignupRequest {

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

    private MultipartFile userProfile;

    private String userType;

}
