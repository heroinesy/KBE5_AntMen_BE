package com.antmen.antwork.domain.review.entity;

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
}