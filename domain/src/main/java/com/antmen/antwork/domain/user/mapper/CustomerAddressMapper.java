package com.antmen.antwork.domain.user.mapper;








@Component
public class CustomerAddressMapper {

    public CustomerAddressResponse toDto(CustomerAddress customerAddress) {
        return CustomerAddressResponse.builder()
                .addressId(customerAddress.getAddressId())
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
                .customerLatitude(customerAddressRequest.getCustomerLatitude())
                .customerLongitude(customerAddressRequest.getCustomerLongitude())
                .build();
    }

}
