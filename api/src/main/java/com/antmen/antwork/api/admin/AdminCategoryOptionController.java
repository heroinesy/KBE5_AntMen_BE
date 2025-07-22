package com.antmen.antwork.api.admin;












@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/admin/category-options")
public class AdminCategoryOptionController {
    private final AdminCategoryOptionsService adminCategoryOptionsService;

    // 옵션 전체 조회 (관리자)
    @GetMapping
    public ResponseEntity<List<CategoryOptionResponseDto>> getAllOptions() {
        List<CategoryOptionResponseDto> result = adminCategoryOptionsService.getAllOptions().stream()
                .map(CategoryOptionResponseDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    // 옵션 단건 조회 (관리자)
    @GetMapping("/{coId}")
    public ResponseEntity<CategoryOptionResponseDto> getOption(@PathVariable Long coId) {
        try {
            CategoryOption o = adminCategoryOptionsService.getOption(coId);
            CategoryOptionResponseDto dto = CategoryOptionResponseDto.fromEntity(o);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 옵션 등록 (관리자)
    @PostMapping
    public ResponseEntity<CategoryOptionResponseDto> createOption(@RequestBody CategoryOptionRequestDto dto) {
        try {
            CategoryOption saved = adminCategoryOptionsService.createOption(
                    dto.getCategoryId(), dto.getCoName(), dto.getCoPrice(), dto.getCoTime());
            return ResponseEntity.ok(CategoryOptionResponseDto.fromEntity(saved));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 옵션 수정 (관리자)
    @PutMapping("/{coId}")
    public ResponseEntity<CategoryOptionResponseDto> updateOption(@PathVariable Long coId, @RequestBody CategoryOptionRequestDto dto) {
        try {
            CategoryOption saved = adminCategoryOptionsService.updateOption(
                    coId, dto.getCoName(), dto.getCoPrice(), dto.getCoTime());
            return ResponseEntity.ok(CategoryOptionResponseDto.fromEntity(saved));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 옵션 삭제 (관리자)
    @DeleteMapping("/{coId}")
    public ResponseEntity<Void> deleteOption(@PathVariable Long coId) {
        try {
            adminCategoryOptionsService.deleteOption(coId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}