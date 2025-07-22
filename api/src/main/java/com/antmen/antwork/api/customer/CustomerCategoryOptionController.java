package com.antmen.antwork.api.customer;

import com.antmen.antwork.domain.category.dto.CategoryOptionResponseDto;
import com.antmen.antwork.domain.category.service.CustomerCategoryOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer/categories")
public class CustomerCategoryOptionController {
    private final CustomerCategoryOptionService customerCategoryOptionService;

    // 카테고리별 옵션 리스트 조회
    @GetMapping("/{categoryId}/options")
    public ResponseEntity<List<CategoryOptionResponseDto>> getOptionsByCategory(@PathVariable Long categoryId) {
        List<CategoryOptionResponseDto> result = customerCategoryOptionService.getOptionsByCategoryId(categoryId);
        return ResponseEntity.ok(result);
    }
}