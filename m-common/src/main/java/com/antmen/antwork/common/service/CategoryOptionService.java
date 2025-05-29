package com.antmen.antwork.common.service;

import com.antmen.antwork.common.domain.entity.CategoryOption;
import com.antmen.antwork.common.domain.entity.Category;
import com.antmen.antwork.common.infra.repository.CategoryOptionRepository;
import com.antmen.antwork.common.infra.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryOptionService {
    private final CategoryOptionRepository categoryOptionRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryOption> getOptionsByCategoryId(Long categoryId) {
        return categoryOptionRepository.findByCategory_CategoryId(categoryId);
    }

    @Transactional(readOnly = true)
    public CategoryOption getOption(Long coId) {
        return categoryOptionRepository.findById(coId)
                .orElseThrow(() -> new IllegalArgumentException("옵션을 찾을 수 없습니다."));
    }

    @Transactional
    public CategoryOption createOption(Long categoryId, String coName, Integer coPrice, Short coTime) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));
        CategoryOption option = CategoryOption.builder()
                .category(category)
                .coName(coName)
                .coPrice(coPrice)
                .coTime(coTime)
                .build();
        return categoryOptionRepository.save(option);
    }

    @Transactional
    public CategoryOption updateOption(Long coId, String coName, Integer coPrice, Short coTime) {
        CategoryOption option = getOption(coId);
        option.setCoName(coName);
        option.setCoPrice(coPrice);
        option.setCoTime(coTime);
        return categoryOptionRepository.save(option);
    }

    @Transactional
    public void deleteOption(Long coId) {
        categoryOptionRepository.deleteById(coId);
    }

    @Transactional(readOnly = true)
    public List<CategoryOption> getAllOptions() {
        return categoryOptionRepository.findAll();
    }
} 