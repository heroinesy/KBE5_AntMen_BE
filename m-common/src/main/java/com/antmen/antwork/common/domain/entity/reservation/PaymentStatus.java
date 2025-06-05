package com.antmen.antwork.common.domain.entity.reservation;

public enum PaymentStatus {
    READY("결제 준비"),
    REQUESTED("결제 요청"),
    IN_PROGRESS("결제 진행중"),
    DONE("결제 완료"),
    CANCELED("결제 취소"),
    FAILED("결제 실패");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
} 