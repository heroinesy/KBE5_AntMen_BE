package com.antmen.antwork.customer.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

}
