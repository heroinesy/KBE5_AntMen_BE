package com.antmen.antwork.domain.user.dto;






@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAccessTokenDto {
    private String access_token;
    private String expires_in;
    private String scope;
    private String id_token;
}
