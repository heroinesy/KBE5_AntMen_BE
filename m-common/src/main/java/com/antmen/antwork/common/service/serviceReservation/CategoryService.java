package com.antmen.antwork.common.service.serviceReservation;

import com.antmen.antwork.common.domain.entity.reservation.Category;
import com.antmen.antwork.common.infra.repository.reservation.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Category getCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));
    }

    @Transactional
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Transactional
    public Category updateCategory(Long id, Category update) {
        Category category = getCategory(id);
        category.setCategoryName(update.getCategoryName());
        category.setCategoryPrice(update.getCategoryPrice());
        category.setCategoryTime(update.getCategoryTime());
        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
} 