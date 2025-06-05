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
@RequestMapping("/category-option")
@RequiredArgsConstructor
public class CategoryOptionController {
    private final CategoryOptionService categoryOptionService;

    // 카테고리별 옵션 리스트 조회
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<CategoryOptionResponseDto>> getOptionsByCategory(@PathVariable Long categoryId) {
        List<CategoryOption> options = categoryOptionService.getOptionsByCategoryId(categoryId);
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

    // 옵션 전체 조회 (관리자)
    @GetMapping
    public ResponseEntity<List<CategoryOptionResponseDto>> getAllOptions() {
        List<CategoryOption> options = categoryOptionService.getAllOptions();
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

    // 옵션 단건 조회 (관리자)
    @GetMapping("/{coId}")
    public ResponseEntity<CategoryOptionResponseDto> getOption(@PathVariable Long coId) {
        try {
            CategoryOption o = categoryOptionService.getOption(coId);
            CategoryOptionResponseDto dto = CategoryOptionResponseDto.builder()
                    .coId(o.getCoId())
                    .coName(o.getCoName())
                    .coPrice(o.getCoPrice())
                    .coTime(o.getCoTime())
                    .build();
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 옵션 등록 (관리자)
    @PostMapping
    public ResponseEntity<CategoryOptionResponseDto> createOption(@RequestBody CategoryOptionRequestDto dto) {
        try {
            CategoryOption saved = categoryOptionService.createOption(
                    dto.getCategoryId(), dto.getCoName(), dto.getCoPrice(), dto.getCoTime());
            CategoryOptionResponseDto response = CategoryOptionResponseDto.builder()
                    .coId(saved.getCoId())
                    .coName(saved.getCoName())
                    .coPrice(saved.getCoPrice())
                    .coTime(saved.getCoTime())
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 옵션 수정 (관리자)
    @PutMapping("/{coId}")
    public ResponseEntity<CategoryOptionResponseDto> updateOption(@PathVariable Long coId, @RequestBody CategoryOptionRequestDto dto) {
        try {
            CategoryOption saved = categoryOptionService.updateOption(
                    coId, dto.getCoName(), dto.getCoPrice(), dto.getCoTime());
            CategoryOptionResponseDto response = CategoryOptionResponseDto.builder()
                    .coId(saved.getCoId())
                    .coName(saved.getCoName())
                    .coPrice(saved.getCoPrice())
                    .coTime(saved.getCoTime())
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 옵션 삭제 (관리자)
    @DeleteMapping("/{coId}")
    public ResponseEntity<Void> deleteOption(@PathVariable Long coId) {
        try {
            categoryOptionService.deleteOption(coId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
} 