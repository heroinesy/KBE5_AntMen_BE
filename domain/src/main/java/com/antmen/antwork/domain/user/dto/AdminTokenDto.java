package com.antmen.antwork.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminTokenDto {
    private String accessToken;
    private long expiresIn;
    private boolean isInitialPassword;
}
