package com.antmen.antwork.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto {
    private String userEmail;
    private String userPassword;
}
