package com.antmen.antwork.common.service.rule;

import org.springframework.stereotype.Component;

/**
 * 사용자 주소록 면적 기준 추천 서비스 시간 (nn평)
 * 10평당 1시간 기준
 * 소수점 첫째 자리 -> 반올림
 * ex. 37평 -> 4시간
 */

@Component
public class ServiceTimeAdvisor {

    /**
     * 면적 기준 추천 서비스 시간
     *
     * @param area 사용자 주소록 기반 면적 (단위 : 평)
     * @return 추천 서비스 시간 (최소 1시간)
     */
    public short recommedTime(int area) {
        if (area <= 0) return 1;

        double remain = area / 10.0;
        int round_remain = (int) Math.round(remain);

        return (short) Math.max(round_remain, 1);
    }
}

// static으로 사용하거나
