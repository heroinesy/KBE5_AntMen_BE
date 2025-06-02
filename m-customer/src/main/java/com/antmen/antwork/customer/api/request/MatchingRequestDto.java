package com.antmen.antwork.customer.api.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class MatchingRequestDto {
    private Long reservationId;
    private List<Long> managerIds;
}
