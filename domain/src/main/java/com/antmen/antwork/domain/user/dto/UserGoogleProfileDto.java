package com.antmen.antwork.domain.user.dto;






@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // 없는 필드는 자동무시
public class UserGoogleProfileDto {
    private String sub;
    private String email;
}
