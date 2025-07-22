package com.antmen.antwork.domain.user.dto;








@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManagerWatingListDto {
    private Long userId;
    private String userName;
    private Integer userAge;
    private String userGender;
    private String userAddress;
    private LocalDateTime userCreatedAt;
    private ManagerStatus managerStatus;
}
