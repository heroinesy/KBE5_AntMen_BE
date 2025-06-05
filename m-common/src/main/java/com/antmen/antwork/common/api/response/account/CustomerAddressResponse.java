package com.antmen.antwork.common.api.response.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerAddressResponse {

    private String addressName;

    private String addressAddr;

    private String addressDetail;

    private Integer addressArea;

}
