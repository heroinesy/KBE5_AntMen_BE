package com.antmen.antwork.domain.user.dto;









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

    //    private MultipartFile userProfile;
    private String userProfile;

    private Integer customerPoint;
}
