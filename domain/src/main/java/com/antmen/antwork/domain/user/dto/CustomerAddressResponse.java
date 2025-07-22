package com.antmen.antwork.domain.user.dto;






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
