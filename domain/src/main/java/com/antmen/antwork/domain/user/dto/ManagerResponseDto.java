package com.antmen.antwork.domain.user.dto;









@Getter
@Builder
public class ManagerResponseDto {
    private String userName;
    private String userTel;
    private String userEmail;
    private String userGender;
    private LocalDate userBirth;
    private String userProfile;
    private String managerAddress;
    private Double managerLatitude;
    private Double managerLongitude;
    private String managerTime;
    private List<ManagerIdFileDto> managerFileUrls;
    private ManagerStatus managerStatus;
    private String rejectReason;
    private String userType;
}
