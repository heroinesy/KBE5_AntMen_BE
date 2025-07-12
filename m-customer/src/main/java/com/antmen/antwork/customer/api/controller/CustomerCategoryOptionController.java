package com.antmen.antwork.customer.api.controller;

import com.antmen.antwork.common.api.response.reservation.CategoryOptionResponseDto;
import com.antmen.antwork.customer.service.CustomerCategoryOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/common/categories")
public class CustomerCategoryOptionController {
    private final CustomerCategoryOptionService customerCategoryOptionService;

    // 카테고리별 옵션 리스트 조회
    @GetMapping("/{categoryId}/options")
    public ResponseEntity<List<CategoryOptionResponseDto>> getOptionsByCategory(@PathVariable Long categoryId) {
        List<CategoryOptionResponseDto> result = customerCategoryOptionService.getOptionsByCategoryId(categoryId);
        return ResponseEntity.ok(result);
    }
}