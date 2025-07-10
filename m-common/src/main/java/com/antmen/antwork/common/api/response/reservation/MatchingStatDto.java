package com.antmen.antwork.common.api.response.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MatchingStatDto {
    private String status; // nothing, ing, fail
    private Long count;
}
