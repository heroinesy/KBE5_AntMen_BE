package com.antmen.antwork.common.api.request.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto {
    private String userLoginId;
    private String userPassword;
}
