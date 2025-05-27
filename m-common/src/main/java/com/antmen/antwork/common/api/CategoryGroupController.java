package com.antmen.antwork.common.api;

import com.antmen.antwork.common.service.CategoryGroupService;
import com.antmen.antwork.common.domain.entity.CategoryGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/category-group")
@RequiredArgsConstructor
public class CategoryGroupController {
    private final CategoryGroupService categoryGroupService;

    // 예약별 카테고리 연결 생성 (여러 카테고리 연결)
    @PostMapping("/reservation/{reservationId}")
    public ResponseEntity<Void> saveCategoryGroups(
            @PathVariable Long reservationId,
            @RequestBody List<Long> categoryIdList) {
        try {
            categoryGroupService.saveCategoryGroups(reservationId, categoryIdList);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 예약ID로 연결된 카테고리ID 리스트 조회
    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<List<Long>> getCategoryIdsByReservationId(@PathVariable Long reservationId) {
        try {
            List<CategoryGroup> groups = categoryGroupService.getCategoryGroupsByReservationId(reservationId);
            List<Long> categoryIds = groups.stream()
                    .map(CategoryGroup::getCategoryId)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(categoryIds);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 