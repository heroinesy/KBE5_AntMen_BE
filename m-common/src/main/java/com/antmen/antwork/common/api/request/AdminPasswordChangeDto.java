package com.antmen.antwork.common.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminPasswordChangeDto {
    private String currentPassword;
    private String newPassword;
}
