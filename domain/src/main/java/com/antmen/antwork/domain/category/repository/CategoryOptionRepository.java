package com.antmen.antwork.domain.category.repository;







@Repository
public interface CategoryOptionRepository extends JpaRepository<CategoryOption, Long> {
    List<CategoryOption> findByCategory_CategoryId(Long categoryId);
} 