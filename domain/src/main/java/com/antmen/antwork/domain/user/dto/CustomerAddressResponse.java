package com.antmen.antwork.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerAddressResponse {

    private Long addressId;

    private String addressName;

    private String addressAddr;

    private String addressDetail;

    private Integer addressArea;

}
