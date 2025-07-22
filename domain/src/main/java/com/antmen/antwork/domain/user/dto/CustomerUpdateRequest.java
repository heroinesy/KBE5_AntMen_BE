package com.antmen.antwork.domain.user.dto;












@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true) // 없는 필드는 자동무시
public class CustomerUpdateRequest {

    private String token;

    @NotBlank
    private String userName;

    @NotBlank
    private String userTel;

    @NotBlank
    private String userEmail;

    @NotNull
    private LocalDate userBirth;

}
