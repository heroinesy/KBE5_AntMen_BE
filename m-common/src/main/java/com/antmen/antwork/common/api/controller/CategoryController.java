package com.antmen.antwork.common.api.controller;

import com.antmen.antwork.common.api.request.reservation.CategoryRequestDto;
import com.antmen.antwork.common.api.response.reservation.CategoryResponseDto;
import com.antmen.antwork.common.domain.entity.reservation.Category;
import com.antmen.antwork.common.service.serviceReservation.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        List<CategoryResponseDto> result = categories.stream()
                .map(c -> CategoryResponseDto.builder()
                        .categoryId(c.getCategoryId())
                        .categoryName(c.getCategoryName())
                        .categoryPrice(c.getCategoryPrice())
                        .categoryTime(c.getCategoryTime())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getCategory(@PathVariable Long id) {
        try {
            Category c = categoryService.getCategory(id);
            CategoryResponseDto dto = CategoryResponseDto.builder()
                    .categoryId(c.getCategoryId())
                    .categoryName(c.getCategoryName())
                    .categoryPrice(c.getCategoryPrice())
                    .categoryTime(c.getCategoryTime())
                    .build();
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(@RequestBody CategoryRequestDto dto) {
        try {
            Category category = Category.builder()
                    .categoryName(dto.getCategoryName())
                    .categoryPrice(dto.getCategoryPrice())
                    .categoryTime(dto.getCategoryTime())
                    .build();
            Category saved = categoryService.createCategory(category);
            CategoryResponseDto response = CategoryResponseDto.builder()
                    .categoryId(saved.getCategoryId())
                    .categoryName(saved.getCategoryName())
                    .categoryPrice(saved.getCategoryPrice())
                    .categoryTime(saved.getCategoryTime())
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable Long id, @RequestBody CategoryRequestDto dto) {
        try {
            Category update = Category.builder()
                    .categoryName(dto.getCategoryName())
                    .categoryPrice(dto.getCategoryPrice())
                    .categoryTime(dto.getCategoryTime())
                    .build();
            Category saved = categoryService.updateCategory(id, update);
            CategoryResponseDto response = CategoryResponseDto.builder()
                    .categoryId(saved.getCategoryId())
                    .categoryName(saved.getCategoryName())
                    .categoryPrice(saved.getCategoryPrice())
                    .categoryTime(saved.getCategoryTime())
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
} 