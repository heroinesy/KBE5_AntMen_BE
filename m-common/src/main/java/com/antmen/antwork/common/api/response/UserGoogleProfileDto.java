package com.antmen.antwork.common.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserGoogleProfileDto {
    private String sub;
    private String email;
}
