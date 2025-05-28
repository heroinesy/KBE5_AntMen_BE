package com.antmen.antwork.common.service.mapper;

import com.antmen.antwork.common.api.response.CategoryResponseDto;
import com.antmen.antwork.common.domain.entity.Category;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    public CategoryResponseDto toDto(Category entity) {
        return CategoryResponseDto.builder()
                .categoryId(entity.getCategoryId())
                .categoryName(entity.getCategoryName())
                .categoryPrice(entity.getCategoryPrice())
                .categoryTime(entity.getCategoryTime())
                .build();
    }

    public List<CategoryResponseDto> toDtoList(List<Category> entities) {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
