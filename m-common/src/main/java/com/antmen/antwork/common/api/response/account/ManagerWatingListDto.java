package com.antmen.antwork.common.api.response.account;

import com.antmen.antwork.common.domain.entity.account.ManagerDetail;
import com.antmen.antwork.common.domain.entity.account.ManagerStatus;
import com.antmen.antwork.common.domain.entity.account.UserGender;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
