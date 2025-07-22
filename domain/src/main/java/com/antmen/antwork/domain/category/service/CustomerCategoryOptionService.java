package com.antmen.antwork.domain.category.service;










@Service
@RequiredArgsConstructor
public class CustomerCategoryOptionService {
    private final CategoryOptionRepository categoryOptionRepository;

    @Transactional(readOnly = true)
    public List<CategoryOptionResponseDto> getOptionsByCategoryId(Long categoryId) {
        return categoryOptionRepository.findByCategory_CategoryId(categoryId).stream()
                .map(CategoryOptionResponseDto::fromEntity)
                .toList();
    }
}