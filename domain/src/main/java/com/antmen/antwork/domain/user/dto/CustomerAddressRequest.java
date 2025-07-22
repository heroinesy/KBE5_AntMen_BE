package com.antmen.antwork.domain.user.dto;








@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerAddressRequest {

    @NotBlank
    private String addressName;

    @NotBlank
    private String addressAddr;

    @NotBlank
    private String addressDetail;

    @NotNull
    private Integer addressArea;

    @NotNull
    private Double customerLatitude;

    @NotNull
    private Double customerLongitude;

}
