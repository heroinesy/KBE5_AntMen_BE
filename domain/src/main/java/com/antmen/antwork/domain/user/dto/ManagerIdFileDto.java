package com.antmen.antwork.domain.user.dto;




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
