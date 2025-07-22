package com.antmen.antwork.domain.category.service;

import com.antmen.antwork.domain.category.dto.CategoryOptionResponseDto;
import com.antmen.antwork.domain.category.repository.CategoryOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerCategoryOptionService {
    private final CategoryOptionRepository categoryOptionRepository;

    @Transactional(readOnly = true)
    public List<CategoryOptionResponseDto> getOptionsByCategoryId(Long categoryId) {
        return categoryOptionRepository.findByCategory_CategoryId(categoryId).stream()
                .map(CategoryOptionResponseDto::fromEntity)
                .toList();
    }
}