package com.antmen.antwork.domain.user.dto;




@Getter
@AllArgsConstructor
public class AdminTokenDto {
    private String accessToken;
    private long expiresIn;
    private boolean isInitialPassword;
}
