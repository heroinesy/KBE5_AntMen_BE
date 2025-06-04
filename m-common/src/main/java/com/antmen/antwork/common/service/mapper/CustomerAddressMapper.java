package com.antmen.antwork.common.service.mapper;

import com.antmen.antwork.common.api.request.CustomerAddressRequest;
import com.antmen.antwork.common.api.response.CustomerAddressResponse;
import com.antmen.antwork.common.domain.entity.CustomerAddress;
import com.antmen.antwork.common.domain.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CustomerAddressMapper {

    public CustomerAddressResponse toDto(CustomerAddress customerAddress) {
        return CustomerAddressResponse.builder()
                .addressName(customerAddress.getAddressName())
                .addressAddr(customerAddress.getAddressAddr())
                .addressDetail(customerAddress.getAddressDetail())
                .addressArea(customerAddress.getAddressArea())
                .build();
    }

    public CustomerAddress toEntity(User user, CustomerAddressRequest customerAddressRequest) {
        return CustomerAddress.builder()
                .user(user)
                .addressName(customerAddressRequest.getAddressName())
                .addressAddr(customerAddressRequest.getAddressAddr())
                .addressDetail(customerAddressRequest.getAddressDetail())
                .addressArea(customerAddressRequest.getAddressArea())
                .build();
    }

}
