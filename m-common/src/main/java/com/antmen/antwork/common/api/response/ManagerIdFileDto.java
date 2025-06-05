package com.antmen.antwork.common.api.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ManagerIdFileDto {
        private Long id;
        private String fileUrl;
}
