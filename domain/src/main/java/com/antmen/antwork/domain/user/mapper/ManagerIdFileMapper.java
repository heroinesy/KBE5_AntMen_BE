package com.antmen.antwork.domain.user.mapper;

import com.antmen.antwork.domain.user.dto.ManagerIdFileDto;
import com.antmen.antwork.domain.user.entity.ManagerIdFile;
import com.antmen.antwork.domain.user.entity.User;
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
