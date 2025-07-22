package com.antmen.antwork.domain.alert.dto;






@Getter
@Setter
@ToString
@Builder
public class AlertRequestDto {
    private Long userId;
    private String alertContent;
    private String alertTrigger;
    private String redirectUrl;
}
