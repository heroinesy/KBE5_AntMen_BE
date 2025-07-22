package com.antmen.antwork.domain.user.dto;






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
