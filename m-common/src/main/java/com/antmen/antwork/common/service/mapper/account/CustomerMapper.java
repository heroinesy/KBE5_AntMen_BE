package com.antmen.antwork.common.service.mapper.account;

import com.antmen.antwork.common.api.request.account.CustomerSignupRequest;
import com.antmen.antwork.common.api.response.account.CustomerProfileResponse;
import com.antmen.antwork.common.domain.entity.account.CustomerDetail;
import com.antmen.antwork.common.domain.entity.account.User;
import com.antmen.antwork.common.domain.entity.account.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
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
                .userPassword(passwordEncoder.encode(request.getUserPassword()))
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
