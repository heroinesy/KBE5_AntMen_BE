package com.antmen.antwork.common.api.response.account;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ManagerIdFileDto {
        private Long id;
        private String managerFileUrl;
        private String originalFileName;
        private String uuidFileName;
        private String extension;
        private String contentType;
}
