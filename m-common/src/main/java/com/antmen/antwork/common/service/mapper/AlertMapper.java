package com.antmen.antwork.common.service.mapper;

import com.antmen.antwork.common.api.request.AlertRequestDto;
import com.antmen.antwork.common.domain.entity.Alert;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AlertMapper {
    @Mapping(target = "isRead", source = "false")
    public Alert toEntity(AlertRequestDto alertRequestDto);
}
