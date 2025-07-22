package com.antmen.antwork.api.customer;









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