package com.antmen.antwork.domain.review.entity;




@Getter
public enum ReviewAuthorType {
    CUSTOMER(UserRole.CUSTOMER),
    MANAGER(UserRole.MANAGER);

    private final UserRole userRole;
    ReviewAuthorType(UserRole userRole) {
        this.userRole = userRole;
    }
}