package com.antmen.antwork.domain.user.dto;







@Getter
@Setter
@NoArgsConstructor
public class ManagerInfoUpdateRequestDto {

    private String userName;

    private String userTel;

    private String userEmail;

    private LocalDate userBirth;

    private String managerAddress;

    private Double managerLatitude;

    private Double managerLongitude;

    private String managerTime;

    private String userType;

}
