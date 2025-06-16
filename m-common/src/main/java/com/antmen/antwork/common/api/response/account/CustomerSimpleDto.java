package com.antmen.antwork.common.api.response.account;

import com.antmen.antwork.common.domain.entity.account.CustomerDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerSimpleDto {
    private String userName;
    private String userEmail;
    private String userType = "고객";
    private int userPoint;

    public CustomerSimpleDto(CustomerDetail customerDetail) {
        this.userName = customerDetail.getUser().getUserName();
        this.userEmail = customerDetail.getUser().getUserEmail();
        this.userPoint = customerDetail.getCustomerPoint();
    }
}
