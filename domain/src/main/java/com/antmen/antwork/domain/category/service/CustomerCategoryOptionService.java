package com.antmen.antwork.domain.category.service;

import com.antmen.antwork.common.api.response.reservation.CategoryOptionResponseDto;
import com.antmen.antwork.common.infra.repository.reservation.CategoryOptionRepository;
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