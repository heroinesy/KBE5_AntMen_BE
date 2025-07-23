package com.antmen.antwork.common.infra.repository.reservation;

import com.antmen.antwork.common.domain.entity.reservation.CategoryOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryOptionRepository extends JpaRepository<CategoryOption, Long> {
    List<CategoryOption> findByCategory_CategoryId(Long categoryId);
} 