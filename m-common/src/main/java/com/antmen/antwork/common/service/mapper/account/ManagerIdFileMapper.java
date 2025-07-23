package com.antmen.antwork.common.service.mapper.account;

import com.antmen.antwork.common.api.response.account.ManagerIdFileDto;
import com.antmen.antwork.common.domain.entity.account.ManagerIdFile;
import com.antmen.antwork.common.domain.entity.account.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class ManagerIdFileMapper {

    public ManagerIdFile toEntity(User user, ManagerIdFileDto dto) {
        return ManagerIdFile.builder()
                .user(user)
                .managerFileUrl(dto.getManagerFileUrl())
                .originalFileName(dto.getOriginalFileName())
                .uuidFileName(dto.getUuidFileName())
                .extension(dto.getExtension())
                .contentType(dto.getContentType())
                .build();
    }

    public ManagerIdFileDto toDto(ManagerIdFile file) {
        return ManagerIdFileDto.builder()
                .id(file.getManagerFileId())
                .managerFileUrl(file.getManagerFileUrl())
                .originalFileName(file.getOriginalFileName())
                .uuidFileName(file.getUuidFileName())
                .extension(file.getExtension())
                .contentType(file.getContentType())
                .build();
    }
}
