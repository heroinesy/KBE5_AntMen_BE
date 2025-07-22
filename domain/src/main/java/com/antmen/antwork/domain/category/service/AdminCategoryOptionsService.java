package com.antmen.antwork.domain.category.service;












@Service
@RequiredArgsConstructor
public class AdminCategoryOptionsService {
    private final CategoryOptionRepository categoryOptionRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryOption> getAllOptions() {
        return categoryOptionRepository.findAll();

    }
    @Transactional(readOnly = true)
    public CategoryOption getOption(Long coId) {
        return categoryOptionRepository.findById(coId)
                .orElseThrow(() -> new NotFoundException("옵션을 찾을 수 없습니다."));
    }

    @Transactional
    public CategoryOption createOption(Long categoryId, String coName, Integer coPrice, Short coTime) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("카테고리를 찾을 수 없습니다."));
        CategoryOption option = CategoryOption.builder()
                .category(category)
                .coName(coName)
                .coPrice(coPrice)
                .coTime(coTime)
                .build();
        return categoryOptionRepository.save(option);
    }

    @Transactional
    public CategoryOption updateOption(Long coId, String coName, Integer coPrice, Short coTime) {
        CategoryOption option = getOption(coId);
        option.setCoName(coName);
        option.setCoPrice(coPrice);
        option.setCoTime(coTime);
        return categoryOptionRepository.save(option);
    }

    @Transactional
    public void deleteOption(Long coId) {
        categoryOptionRepository.deleteById(coId);
    }
}
