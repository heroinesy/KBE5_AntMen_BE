package com.antmen.antwork.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminPasswordChangeDto {
    private String currentPassword;
    private String newPassword;
}
