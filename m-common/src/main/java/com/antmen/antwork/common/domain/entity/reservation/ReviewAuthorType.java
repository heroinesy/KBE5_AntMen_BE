package com.antmen.antwork.common.domain.entity.reservation;

import com.antmen.antwork.common.domain.entity.account.User;
import com.antmen.antwork.common.domain.entity.account.UserRole;
import lombok.Getter;

@Getter
public enum ReviewAuthorType {
    CUSTOMER(UserRole.CUSTOMER),
    MANAGER(UserRole.MANAGER);

    private final UserRole userRole;
    ReviewAuthorType(UserRole userRole) {
        this.userRole = userRole;
    }

    public User getAuthorUser(Reservation reservation) {
        return this == CUSTOMER ? reservation.getCustomer() : reservation.getManager();
    }

    public UserRole getUserRole() {
        return userRole;
    }
}
