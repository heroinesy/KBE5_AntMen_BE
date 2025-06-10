package com.antmen.antwork.common.api.controller;

import com.antmen.antwork.common.api.request.reservation.CategoryOptionRequestDto;
import com.antmen.antwork.common.api.response.reservation.CategoryOptionResponseDto;
import com.antmen.antwork.common.domain.entity.reservation.CategoryOption;
import com.antmen.antwork.common.service.serviceReservation.CategoryOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/common/categories")
public class CategoryOptionController {
    private final CategoryOptionService categoryOptionService;

    // 카테고리별 옵션 리스트 조회
    @GetMapping("/{coId}/options")
    public ResponseEntity<List<CategoryOptionResponseDto>> getOptionsByCategory(@PathVariable Long coId) {
        List<CategoryOption> options = categoryOptionService.getOptionsByCategoryId(coId);
        List<CategoryOptionResponseDto> result = options.stream()
                .map(o -> CategoryOptionResponseDto.builder()
                        .coId(o.getCoId())
                        .coName(o.getCoName())
                        .coPrice(o.getCoPrice())
                        .coTime(o.getCoTime())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
} 