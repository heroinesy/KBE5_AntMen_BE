package com.antmen.antwork.common.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlertTrigger {

    MATCHING_REQUEST_TO_MANAGER("새로운 매칭 요청이 도착했습니다."),
    MATCHING_ACCEPTED_BY_MANAGER("매칭이 완료되었습니다. 예약을 확정해 주세요."),
    MATCHING_CONFIRMED_BY_CUSTOMER("매칭이 확정되었습니다. '내 업무'에서 확인해 주세요."),
    MATCHING_LOST_TO_MANAGER("다른 매니저와 매칭이 완료되었습니다."),

    RESERVATION_CONFIRMED("예약이 완료되었습니다."),
    RESERVATION_CANCELED("예약이 취소되었습니다."),

    SERVICE_CHECK_IN("매니저가 체크인하여 서비스를 시작했습니다. 안심하고 맡겨주세요."),
    SERVICE_CHECK_OUT("매니저가 청소를 마치고 체크아웃했습니다. 확인 후 리뷰를 남겨주세요.");

//    NOTICE_NEW("공지사항 안내드립니다."),
//    COMMENT_ON_POST("댓글이 달렸습니다.");

    private final String content;
}
