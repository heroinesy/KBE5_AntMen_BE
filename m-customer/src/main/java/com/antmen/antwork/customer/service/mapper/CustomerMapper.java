package com.antmen.antwork.customer.service.mapper;

import com.antmen.antwork.common.domain.entity.User;
import com.antmen.antwork.common.domain.entity.UserRole;
import com.antmen.antwork.customer.api.request.CustomerSignupRequest;
import com.antmen.antwork.customer.api.response.CustomerProfileResponse;
import com.antmen.antwork.customer.domain.entity.CustomerDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerMapper {

    private final PasswordEncoder passwordEncoder;

    public CustomerProfileResponse toDto(User user, CustomerDetail customerDetail) {
        return CustomerProfileResponse.builder()
                .userName(user.getUserName())
                .userTel(user.getUserTel())
                .userEmail(user.getUserEmail())
                .userGender(user.getUserGender())
                .userBirth(user.getUserBirth())
                .userProfile(user.getUserProfile())
                .customerPoint(customerDetail.getCustomerPoint())
                .build();
    }

    public User toUserEntity(CustomerSignupRequest request) {
        return User.builder()
                .userLoginId(request.getUserLoginId())
                .userPassword(request.getUserPassword()) // 암호화 필요
                .userName(request.getUserName())
                .userTel(request.getUserTel())
                .userEmail(request.getUserEmail())
                .userBirth(request.getUserBirth())
                .userGender(request.getUserGender())
                .userProfile(request.getUserProfile())
                .userRole(UserRole.CUSTOMER)
                .build();
    }

    public CustomerDetail toCustomerDetailEntity(User user) {
        return CustomerDetail.builder()
                .user(user)
                .customerPoint(0)
                .build();
    }

}
