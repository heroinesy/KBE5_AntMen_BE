package com.antmen.antwork.common.domain.entity;

public enum ReservationStatus {
    WAITING("W"),
    MATCHING("M"),
    PAY("P"),
    DONE("D"),
    CANCEL("C"),
    ERROR("E");

    private final String code;

    ReservationStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static ReservationStatus fromCode(String code) {
        for (ReservationStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 예약 상태 코드입니다 : " + code);
    }

    public static boolean isValidCode(String code) {
        for (ReservationStatus status : values()) {
            if (status.code.equals(code)) {
                return true;
            }
        }
        return false;
    }

}
